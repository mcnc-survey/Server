storage "file" {
  path = "/vault/data"
}

listener "tcp" {
  address       = "0.0.0.0:8200"
  tls_disable   = true
}

ui = true

# 초기 설정 및 봉인 해제 최대 시도 횟수
max_lease_ttl         = "768h"
default_lease_ttl    = "768h"
api_addr             = "http://0.0.0.0:8200"
cluster_addr         = "http://0.0.0.0:8201"

# 로깅 설정
log_level = "info"
log_format = "standard"

# 작업 디렉토리 및 PID 파일
pid_file = "/vault/data/vault.pid"

# 메모리 잠금 비활성화 (필요에 따라 조정)
disable_mlock = true