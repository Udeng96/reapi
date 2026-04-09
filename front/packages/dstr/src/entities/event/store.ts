import { create } from 'zustand';
import type { Search, EventsPayload } from './types';
import { DEFAULT_START_DTM, DTM_OPTION } from './consts';

interface EventViewState {
  // 검색 필터
  checkedLocaList: Search[];
  checkedTypeList: Search[];
  checkedModalLocaList: Search[];
  checkedModalTypeList: Search[];

  // 검색 박스 활성 상태 ('none' | 'loca' | 'type')
  activeSearchBox: string;

  // 모달
  activeModal: boolean;

  // 선택된 이벤트 ID
  activeItem: string | null;
  activeModalItem: string | null;

  // 쿼리 파라미터
  eventPayload: EventsPayload;
  modalPayload: EventsPayload;

  // 이벤트 유형 필터
  activeEventType: string | null;

  // WebSocket 이벤트 수신 여부
  wsEventReceive: boolean;

  // Actions
  setCheckedLocaList: (list: Search[]) => void;
  setCheckedTypeList: (list: Search[]) => void;
  setCheckedModalLocaList: (list: Search[]) => void;
  setCheckedModalTypeList: (list: Search[]) => void;
  setActiveSearchBox: (box: string) => void;
  setActiveModal: (open: boolean) => void;
  setActiveItem: (id: string | null) => void;
  setActiveModalItem: (id: string | null) => void;
  setEventPayload: (payload: EventsPayload) => void;
  setModalPayload: (payload: EventsPayload) => void;
  setActiveEventType: (type: string | null) => void;
  setWsEventReceive: (receive: boolean) => void;
}

export const useEventStore = create<EventViewState>((set) => ({
  checkedLocaList: [],
  checkedTypeList: [],
  checkedModalLocaList: [],
  checkedModalTypeList: [],
  activeSearchBox: 'none',
  activeModal: false,
  activeItem: null,
  activeModalItem: null,
  eventPayload: {
    eventIdList: null,
    locationCdList: null,
    startDtm: DEFAULT_START_DTM,
    endDtm: null,
    page: 1,
    rows: 10,
  },
  modalPayload: {
    eventIdList: null,
    locationCdList: null,
    startDtm: DTM_OPTION[3].startDtm,
    endDtm: DTM_OPTION[3].endDtm,
    page: 1,
    rows: 10,
  },
  activeEventType: null,
  wsEventReceive: true,

  setCheckedLocaList: (list) => set({ checkedLocaList: list }),
  setCheckedTypeList: (list) => set({ checkedTypeList: list }),
  setCheckedModalLocaList: (list) => set({ checkedModalLocaList: list }),
  setCheckedModalTypeList: (list) => set({ checkedModalTypeList: list }),
  setActiveSearchBox: (box) => set({ activeSearchBox: box }),
  setActiveModal: (open) => set({ activeModal: open }),
  setActiveItem: (id) => set({ activeItem: id }),
  setActiveModalItem: (id) => set({ activeModalItem: id }),
  setEventPayload: (payload) => set({ eventPayload: payload }),
  setModalPayload: (payload) => set({ modalPayload: payload }),
  setActiveEventType: (type) => set({ activeEventType: type }),
  setWsEventReceive: (receive) => set({ wsEventReceive: receive }),
}));
