terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

# 변수 정의
variable "region" {
  description = "AWS 리전"
  type        = string
  default     = "ap-northeast-2"
}

variable "docker_hub_username" {
  description = "Docker Hub 사용자명"
  type        = string
  default     = "uheejoon"
}

variable "instance_type" {
  description = "EC2 인스턴스 타입"
  type        = string
  default     = "t3.micro"
}

variable "ami_id" {
  description = "Amazon Linux 2 AMI ID"
  type        = string
  default     = "ami-0bcdae8006538619a"
}

# Provider 설정
provider "aws" {
  region = var.region
}

# VPC 및 서브넷 설정
resource "aws_vpc" "survey_vpc" {
  cidr_block           = "10.0.0.0/16"  # VPC CIDR 범위 지정
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "Survey-MSA-VPC"
  }
}

resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.survey_vpc.id
  cidr_block              = "10.0.1.0/24"  # 퍼블릭 서브넷
  map_public_ip_on_launch = true

  tags = {
    Name = "Public-Subnet"
  }
}

resource "aws_subnet" "private" {
  vpc_id     = aws_vpc.survey_vpc.id
  cidr_block = "10.0.2.0/24"  # 프라이빗 서브넷

  tags = {
    Name = "Private-Subnet"
  }
}

# 인터넷 게이트웨이 설정 (중복 방지)
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.survey_vpc.id
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.survey_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
}

resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table" "private" {
  vpc_id = aws_vpc.survey_vpc.id
}

resource "aws_route_table_association" "private" {
  subnet_id      = aws_subnet.private.id
  route_table_id = aws_route_table.private.id
}

# 보안 그룹 설정
resource "aws_security_group" "gateway" {
  name        = "Gateway-SG"
  description = "Allow inbound traffic for gateway"
  vpc_id      = aws_vpc.survey_vpc.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # 필요시 IP 제한 권장
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "private" {
  name        = "Private-SG"
  description = "Allow internal communication"
  vpc_id      = aws_vpc.survey_vpc.id

  ingress {
    from_port       = 0
    to_port         = 65535
    protocol        = "tcp"
    security_groups = [aws_security_group.gateway.id] # 같은 보안 그룹 간 통신 허용
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# EC2 키 페어 생성
resource "aws_key_pair" "survey_msa_key" {
  key_name   = "survey-msa-key"
  public_key = file(pathexpand("~/.ssh/id_rsa.pub"))
}

# 마이크로서비스 목록
locals {
  microservices = [
#     "survey-gateway",
    "survey-discovery",
    "survey-config-server",
    "survey-service",
    "survey-admin-service",
    "survey-respondent-service",
    "survey-response-service"
  ]
}

# Gateway 인스턴스
resource "aws_instance" "gateway" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = aws_key_pair.survey_msa_key.key_name
  subnet_id     = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.gateway.id]

  tags = {
    Name = "survey-gateway"
    Role = "Gateway"
  }
}

# 다른 마이크로서비스 인스턴스
resource "aws_instance" "other_microservices" {
  count         = length(local.microservices)
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = aws_key_pair.survey_msa_key.key_name
  subnet_id     = aws_subnet.private.id
  vpc_security_group_ids = [aws_security_group.private.id]

  tags = {
    Name = local.microservices[count.index]
    Role = "Microservice"
  }
}

# Docker Compose 설정 (선택적)
resource "null_resource" "docker_compose_setup" {
  count = length(local.microservices)

  triggers = {
    instance_id = aws_instance.other_microservices[count.index].id
  }

  connection {
    type = "ssh"
    user = "ubuntu"
    private_key = file(pathexpand("~/.ssh/id_rsa"))
    host = aws_instance.other_microservices[count.index].public_ip
  }

  provisioner "file" {
    content = templatefile("${path.module}/docker-compose.tpl", {
      service_name        = local.microservices[count.index]
      docker_hub_username = var.docker_hub_username
    })
    destination = "/home/ubuntu/docker-compose.yaml"
  }

  provisioner "remote-exec" {
    inline = [
      "docker-compose -f /home/ubuntu/docker-compose.yaml up -d"
    ]
  }

  depends_on = [aws_instance.other_microservices]
}

# 출력값
output "gateway_public_ip" {
  value = aws_instance.gateway.public_ip
}

output "microservices_private_ips" {
  value = {
    for idx, instance in aws_instance.other_microservices :
    local.microservices[idx] => instance.private_ip
  }
}
