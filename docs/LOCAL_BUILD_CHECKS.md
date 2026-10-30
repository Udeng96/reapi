# Local build validation — 2026-10-30

cd front
pnpm install --frozen-lockfile --ignore-scripts
pnpm build

Java: Gradle 8.5 + JDK 17: :hams:test :dstr:compileJava :cctv-monitor:compileJava. The local portable validation ran existing HAMS unit tests. Legacy frontend source/build script is preserved as build:legacy. No runtime device/DB connection was used.

Device access is disabled by default. Supply HAMS_ID, HAMS_PW and HAMS_CRYPT_KEY externally, then explicitly enable HAMS_TCP_ENABLED only in your private runtime configuration. Facility fixtures use synthetic names, zero coordinates and documentation addresses. Existing history may contain old values; rotate any credentials or keys still in use.
