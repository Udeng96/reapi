import dayjs from 'dayjs';

export const DEFAULT_START_DTM = '20201212';

export const DTM_OPTION = [
  { id: '오늘', startDtm: dayjs().format('YYYYMMDD'), endDtm: dayjs().format('YYYYMMDD') },
  { id: '1주일', startDtm: dayjs().subtract(7, 'day').format('YYYYMMDD'), endDtm: dayjs().format('YYYYMMDD') },
  { id: '1개월', startDtm: dayjs().subtract(1, 'month').format('YYYYMMDD'), endDtm: dayjs().format('YYYYMMDD') },
  { id: '3개월', startDtm: dayjs().subtract(3, 'month').format('YYYYMMDD'), endDtm: dayjs().format('YYYYMMDD') },
  { id: '6개월', startDtm: dayjs().subtract(6, 'month').format('YYYYMMDD'), endDtm: dayjs().format('YYYYMMDD') },
];

export const MAX_CCTV_PLAYER_CNT = 4;
