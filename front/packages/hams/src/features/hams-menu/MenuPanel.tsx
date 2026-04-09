import { useEffect, useState } from 'react';
import { useHamsList, useHamsDetail } from '../../entities/hams/queries';
import { useHamsStore } from '../../entities/hams/store';
import type { HamsGisFacInfo } from '../../entities/hams/types';

function MenuIcon({
  item,
  iconNm,
  isActive,
  onClick,
}: {
  item: HamsGisFacInfo;
  iconNm: string;
  isActive: boolean;
  onClick: (item: HamsGisFacInfo) => void;
}) {
  const [showTooltip, setShowTooltip] = useState(false);
  const isConnected = item.status?.isConnect ?? false;
  const stateClass = isConnected ? 'state_connected' : 'state_disconnected';

  const displayNm = item.facNm?.includes('BBS')
    ? '동탄' + item.facNm
    : item.facNm;

  return (
    <li
      className={isActive ? `active ${stateClass}` : stateClass}
      data-icon={iconNm}
      onClick={() => onClick(item)}
    >
      <a
        onMouseEnter={() => setShowTooltip(true)}
        onMouseLeave={() => setShowTooltip(false)}
      >
        {displayNm}
      </a>
      <div
        className="hams tooltip_text"
        style={{ display: showTooltip ? 'block' : 'none' }}
      >
        {item.facNm}
      </div>
    </li>
  );
}

export function MenuPanel() {
  const { selectedHams, setSelectedHams } = useHamsStore();
  const { data: hamsList } = useHamsList();

  // 첫 번째 국사 자동 선택
  useEffect(() => {
    if (hamsList && hamsList.length > 0 && !selectedHams) {
      setSelectedHams(hamsList[0]);
    }
  }, [hamsList]);

  const handleClickMenu = (item: HamsGisFacInfo) => {
    setSelectedHams(item);
  };

  return (
    <section className="hams menu_container">
      <h1 className="container_title">
        무인국사<br />
        <span>Dashboard</span>
      </h1>
      <nav className="nav_menu">
        <ul className="nav_menu_list">
          {hamsList?.map((item, index) => (
            <MenuIcon
              key={item.facId ?? index}
              item={item}
              iconNm={`icon${index}`}
              isActive={selectedHams?.facId === item.facId}
              onClick={handleClickMenu}
            />
          ))}
        </ul>
      </nav>
    </section>
  );
}
