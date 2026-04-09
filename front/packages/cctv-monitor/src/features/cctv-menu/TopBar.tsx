import { useMonitorStore } from '../../entities/monitor/store';

export function TopBar() {
  const { isMenuOpen } = useMonitorStore();

  return (
    <section className="top_bar" style={{ top: isMenuOpen ? '0px' : '-56px' }}>
      <div className="logo_img" />
      <button className="btn_logout" onClick={() => { /* TODO: 로그아웃 */ }}>
        로그아웃
      </button>
      <div className="user_txt">
        <span>관리자</span>님 환영합니다
      </div>
    </section>
  );
}
