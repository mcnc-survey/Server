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

# Elastic IP (EIP) 생성
resource "aws_eip" "nat_eip" {
  vpc = true
}

# NAT Gateway 생성
resource "aws_nat_gateway" "nat_gateway" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public.id  # NAT Gateway는 퍼블릭 서브넷에 배치되어야 합니다.

  tags = {
    Name = "Survey-NAT-Gateway"
  }
}

resource "aws_route_table" "private" {
  vpc_id = aws_vpc.survey_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway.id  # NAT Gateway를 통해 인터넷을 라우팅
  }
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

  # 규칙: HTTPS (TCP 443) 포트에 대한 192.168.0.0/24 허용  - SSM
  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["192.168.0.0/24"]
  }
}

resource "aws_security_group" "ssm_endpoints" {
  name        = "ssm-endpoints-sg"
  description = "Security group for SSM VPC Endpoints"
  vpc_id      = aws_vpc.survey_vpc.id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = [aws_vpc.survey_vpc.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["192.168.0.0/24"]
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

# IAM 역할 생성
resource "aws_iam_role" "ssm_instance_role" {
  name = "ssm-instance-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

# SSM 관리 정책 추가 (이미 있다면 더 추가)
resource "aws_iam_role_policy_attachment" "ssm_core_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
  role       = aws_iam_role.ssm_instance_role.name
}

# VPC 엔드포인트용 정책 추가 (선택적)
# resource "aws_iam_role_policy_attachment" "ssm_vpc_endpoint_policy" {
#   policy_arn = "arn:aws:iam::aws:policy/AmazonSSMVPCEndpointAccess"
#   role       = aws_iam_role.ssm_instance_role.name
# }

# SSM 전체 접근 정책 추가
resource "aws_iam_role_policy_attachment" "ssm_full_access" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMFullAccess"
  role       = aws_iam_role.ssm_instance_role.name
}

resource "aws_vpc_endpoint" "ssm" {
  vpc_id             = aws_vpc.survey_vpc.id
  service_name       = "com.amazonaws.${var.region}.ssm"
  vpc_endpoint_type  = "Interface"
  subnet_ids         = [aws_subnet.private.id]
  security_group_ids = [aws_security_group.ssm_endpoints.id]
}

resource "aws_vpc_endpoint" "ssmmessages" {
  vpc_id             = aws_vpc.survey_vpc.id
  service_name       = "com.amazonaws.${var.region}.ssmmessages"
  vpc_endpoint_type  = "Interface"
  subnet_ids         = [aws_subnet.private.id]
  security_group_ids = [aws_security_group.ssm_endpoints.id]
}

resource "aws_vpc_endpoint" "ec2messages" {
  vpc_id             = aws_vpc.survey_vpc.id
  service_name       = "com.amazonaws.${var.region}.ec2messages"
  vpc_endpoint_type  = "Interface"
  subnet_ids         = [aws_subnet.private.id]
  security_group_ids = [aws_security_group.ssm_endpoints.id]
}

# IAM 인스턴스 프로파일 생성
resource "aws_iam_instance_profile" "ssm_profile" {
  name = "ssm-instance-profile"
  role = aws_iam_role.ssm_instance_role.name
}

# Gateway 인스턴스
resource "aws_instance" "gateway" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = aws_key_pair.survey_msa_key.key_name
  subnet_id     = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.gateway.id]

  # IAM 인스턴스 프로파일 추가
  iam_instance_profile = aws_iam_instance_profile.ssm_profile.name

  tags = {
    Name = "survey-gateway"
    Role = "Gateway"
  }
}

# Gateway 인스턴스
resource "aws_instance" "response-gateway" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = aws_key_pair.survey_msa_key.key_name
  subnet_id     = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.gateway.id]

  # IAM 인스턴스 프로파일 추가
  iam_instance_profile = aws_iam_instance_profile.ssm_profile.name

  tags = {
    Name = "survey-response-gateway"
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

  # IAM 인스턴스 프로파일 추가
  iam_instance_profile = aws_iam_instance_profile.ssm_profile.name

  tags = {
    Name = local.microservices[count.index]
    Role = "Microservice"
  }
}

# Gateway 인스턴스에 Docker 설치
resource "null_resource" "docker_setup_gateway" {
  triggers = {
    instance_id = aws_instance.gateway.id
  }

  connection {
    type        = "ssh"
    user        = "ubuntu"
    private_key = file(pathexpand("~/.ssh/id_rsa"))
    host        = aws_instance.gateway.public_ip
  }

  provisioner "remote-exec" {
    inline = [
      # Docker 설치를 위한 기본 패키지 업데이트
      "sudo apt-get update",
      "sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common",

      # Docker GPG 키 추가
      "curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -",

      # Docker 리포지토리 추가
      "sudo add-apt-repository -y 'deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable'",

      # Docker 설치
      "sudo apt-get update",
      "sudo apt-get install -y docker-ce docker-ce-cli containerd.io",

      # Docker Compose 설치
      "sudo curl -L \"https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)\" -o /usr/local/bin/docker-compose",
      "sudo chmod +x /usr/local/bin/docker-compose",

      # 현재 사용자를 docker 그룹에 추가
      "sudo usermod -aG docker ubuntu"
    ]
  }

  depends_on = [aws_instance.gateway]
}

# Gateway 인스턴스에 Docker 설치
resource "null_resource" "docker_setup_response_gateway" {
  triggers = {
    instance_id = aws_instance.response-gateway.id
  }

  connection {
    type        = "ssh"
    user        = "ubuntu"
    private_key = file(pathexpand("~/.ssh/id_rsa"))
    host        = aws_instance.response-gateway.public_ip
  }

  provisioner "remote-exec" {
    inline = [
      # Docker 설치를 위한 기본 패키지 업데이트
      "sudo apt-get update",
      "sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common",

      # Docker GPG 키 추가
      "curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -",

      # Docker 리포지토리 추가
      "sudo add-apt-repository -y 'deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable'",

      # Docker 설치
      "sudo apt-get update",
      "sudo apt-get install -y docker-ce docker-ce-cli containerd.io",

      # Docker Compose 설치
      "sudo curl -L \"https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)\" -o /usr/local/bin/docker-compose",
      "sudo chmod +x /usr/local/bin/docker-compose",

      # 현재 사용자를 docker 그룹에 추가
      "sudo usermod -aG docker ubuntu"
    ]
  }

  depends_on = [aws_instance.response-gateway]
}

# Other Microservices에 Docker 및 SSM Agent 설치
resource "null_resource" "docker_setup_microservices" {
  count = length(local.microservices)

  triggers = {
    instance_id = aws_instance.other_microservices[count.index].id
  }

  provisioner "local-exec" {
    command = "aws ssm send-command --document-name \"AWS-RunShellScript\" --instance-ids \"${aws_instance.other_microservices[count.index].id}\" --parameters '{\"commands\":[\"sudo apt-get update\", \"sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common\", \"sudo snap install amazon-ssm-agent --classic\", \"sudo systemctl enable amazon-ssm-agent\", \"sudo systemctl start amazon-ssm-agent\", \"curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -\", \"sudo add-apt-repository -y \\\"deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable\\\"\", \"sudo apt-get update\", \"sudo apt-get install -y docker-ce docker-ce-cli containerd.io\", \"sudo curl -L \\\"https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)\\\" -o /usr/local/bin/docker-compose\", \"sudo chmod +x /usr/local/bin/docker-compose\", \"sudo usermod -aG docker ubuntu\"]}'"
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
