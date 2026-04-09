#!/bin/bash

export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_DIR="$ROOT_DIR/logs"
mkdir -p "$LOG_DIR"

start() {
  echo "=== Backend 전체 시작 ==="

  echo "[1/3] cctv-monitor (port 30003) 시작..."
  $ROOT_DIR/gradlew :cctv-monitor:bootRun > "$LOG_DIR/cctv-monitor.log" 2>&1 &
  echo $! > "$LOG_DIR/cctv-monitor.pid"

  echo "[2/3] hams (port 30002) 시작..."
  $ROOT_DIR/gradlew :hams:bootRun > "$LOG_DIR/hams.log" 2>&1 &
  echo $! > "$LOG_DIR/hams.pid"

  echo "[3/3] dstr (port 30001) 시작..."
  $ROOT_DIR/gradlew :dstr:bootRun > "$LOG_DIR/dstr.log" 2>&1 &
  echo $! > "$LOG_DIR/dstr.pid"

  echo ""
  echo "모든 모듈 백그라운드 실행 중"
  echo "로그: $LOG_DIR/"
  echo "종료: ./run-all.sh stop"
}

stop() {
  echo "=== Backend 전체 종료 ==="

  for module in cctv-monitor hams dstr; do
    PID_FILE="$LOG_DIR/$module.pid"
    if [ -f "$PID_FILE" ]; then
      PID=$(cat "$PID_FILE")
      if kill -0 "$PID" 2>/dev/null; then
        kill "$PID"
        echo "[$module] 종료 (PID: $PID)"
      else
        echo "[$module] 이미 종료됨"
      fi
      rm -f "$PID_FILE"
    else
      echo "[$module] PID 파일 없음"
    fi
  done
}

status() {
  echo "=== Backend 상태 ==="
  for module in cctv-monitor hams dstr; do
    PID_FILE="$LOG_DIR/$module.pid"
    if [ -f "$PID_FILE" ]; then
      PID=$(cat "$PID_FILE")
      if kill -0 "$PID" 2>/dev/null; then
        echo "[$module] 실행 중 (PID: $PID)"
      else
        echo "[$module] 중단됨"
      fi
    else
      echo "[$module] 미실행"
    fi
  done
}

case "$1" in
  start)  start ;;
  stop)   stop ;;
  status) status ;;
  *)
    echo "사용법: ./run-all.sh [start|stop|status]"
    ;;
esac
