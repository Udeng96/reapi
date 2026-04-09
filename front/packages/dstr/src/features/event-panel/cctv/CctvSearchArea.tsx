import { useState } from 'react';
import dayjs from 'dayjs';
import Select, { type StylesConfig } from 'react-select';
import { useCctvStore } from '../../../entities/cctv/store';
import { useCctvClfy } from '../../../entities/cctv/queries';
import type { CctvClfy } from '../../../entities/cctv/types';

type SelectOption = { value: string; label: string };

const selectStyle: StylesConfig<SelectOption, false> = {
  option: (provided, state) => ({
    ...provided,
    color: '#fafcfc',
    backgroundColor: state.isSelected ? '#452019' : '#11111a',
    paddingTop: 8,
    paddingLeft: 17,
    height: '30px',
    '&:hover': {
      backgroundColor: state.isSelected ? '#542613' : '#282a38',
    },
  }),
  dropdownIndicator: (provided) => ({
    ...provided,
    color: '#c7c8c9',
    padding: '6px 9px 8px 8px',
    '&:hover': { color: '#ff6f26' },
  }),
  control: (provided, state) => ({
    ...provided,
    width: '382px',
    height: '38px',
    minHeight: '38px',
    color: '#fafcfc',
    borderRadius: '6px',
    backgroundColor: state.isFocused ? '#11111a' : '#22232e',
    borderColor: state.isFocused ? '#db5c18' : '#3d3f4d',
    boxShadow: 'none',
    '&:hover': {
      borderColor: state.isFocused ? '#db5c18' : '#4f5163',
      backgroundColor: state.isFocused ? '#11111a' : '#2a2b38',
    },
  }),
  menu: (provided) => ({
    ...provided,
    color: '#f7f7f7',
    backgroundColor: '#11111a',
    border: '1px solid #db5c18',
    borderRadius: '6px',
    fontSize: '12px',
    fontFamily: 'NotoSansCJKR',
    margin: '1px 0',
    boxShadow: 'none',
  }),
  menuList: (base) => ({
    ...base,
    '::-webkit-scrollbar': { width: '3px', height: '0px' },
    '::-webkit-scrollbar-track': { background: 'rgba(235,242,245,0.1)', borderRadius: '1.5px' },
    '::-webkit-scrollbar-thumb': { background: '#747a8a', borderRadius: '1.5px' },
    '::-webkit-scrollbar-thumb:hover': { background: '#9197a8' },
  }),
  indicatorSeparator: (provided) => ({ ...provided, backgroundColor: 'transparent' }),
  singleValue: (provided) => ({
    ...provided,
    fontFamily: 'NotoSansCJKR',
    fontSize: '12px',
    color: '#f7f7f7',
    paddingLeft: 7,
    paddingBottom: 2,
  }),
  placeholder: (defaultStyles) => ({
    ...defaultStyles,
    fontFamily: 'NotoSansCJKR',
    fontSize: '12px',
    color: 'rgba(255,255,255,0.4)',
    paddingLeft: 7,
  }),
};

const FIRST_OPTION: SelectOption = { value: '', label: '전체' };

interface Props {
  setCurrentPage: (page: number) => void;
}

export function CctvSearchArea({ setCurrentPage }: Props) {
  const [date, setDate] = useState(dayjs().format('YYYY-MM-DD HH:mm:ss'));
  const [localWord, setLocalWord] = useState('');
  const [localClfy, setLocalClfy] = useState<SelectOption>(FIRST_OPTION);

  const setSelectClfy = useCctvStore((s) => s.setSelectClfy);
  const setWord = useCctvStore((s) => s.setWord);

  const { data: clfyList } = useCctvClfy();

  const options: SelectOption[] = [
    FIRST_OPTION,
    ...(clfyList?.map((c) => ({ value: c.id, label: c.nm })) ?? []),
  ];

  const doSearch = () => {
    setDate(dayjs().format('YYYY-MM-DD HH:mm:ss'));
    setSelectClfy({ id: localClfy.value, nm: localClfy.label } as CctvClfy);
    setWord(localWord);
    setCurrentPage(1);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') doSearch();
  };

  return (
    <div className="search_setting_area">
      <h2 className="dash_sub_title cctv_search_title">CCTV 검색 조건 설정</h2>

      {/* 설치 목적 */}
      <div className="select_block">
        <p className="select_title">
          <span>&middot;</span>설치 목적
        </p>
        <Select<SelectOption, false>
          options={options}
          styles={selectStyle}
          value={localClfy}
          defaultValue={FIRST_OPTION}
          onChange={(opt) => setLocalClfy(opt ?? FIRST_OPTION)}
          isSearchable={false}
        />
      </div>

      {/* 명칭 입력 */}
      <div className="select_block">
        <p className="select_title">
          <span>&middot;</span>명칭 입력
        </p>
        <input
          type="text"
          className="search_input"
          placeholder="검색할 명칭을 입력해 주세요"
          value={localWord}
          onChange={(e) => setLocalWord(e.target.value)}
          onKeyDown={handleKeyDown}
        />
      </div>

      <button type="button" className="btn_search" onClick={doSearch}>
        검색<span>(업데이트 일시 : {date})</span>
      </button>
    </div>
  );
}
