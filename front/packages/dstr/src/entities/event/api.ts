import axios from 'axios';
import type { EventResult, EventsPayload, Search, VwEvent, HeatmapClfy, HeatmapCount, DstrInfo, FireCarInfo } from './types';

const BASE = '/api';

export const eventApi = {
  /** 이벤트 목록 조회 (페이징) */
  fetchEvents: async (payload: EventsPayload): Promise<EventResult> => {
    const { data } = await axios.get(`${BASE}/event`, { params: payload });
    return data;
  },

  /** 이벤트 단건 조회 */
  fetchEvent: async (id: string): Promise<VwEvent> => {
    const { data } = await axios.get(`${BASE}/event/${id}`);
    return data;
  },

  /** uSvcOutbId로 이벤트 조회 */
  fetchEventByUSvcId: async (id: string): Promise<VwEvent> => {
    const { data } = await axios.get(`${BASE}/event/uSvcOutbId/${id}`);
    return data;
  },

  /** 이벤트 유형 목록 */
  fetchEventTypes: async (): Promise<Search[]> => {
    const { data } = await axios.get(`${BASE}/event/type`);
    return data;
  },

  /** 지역 목록 */
  fetchLocations: async (): Promise<Search[]> => {
    const { data } = await axios.get(`${BASE}/event/location`);
    return data;
  },

  /** 히트맵 분류 목록 */
  fetchHeatmapClfy: async (): Promise<HeatmapClfy[]> => {
    const { data } = await axios.get(`${BASE}/event/heatmap/clfy`);
    return data;
  },

  /** 히트맵 카운트 */
  fetchHeatmapCount: async (params: {
    svcThemeCd: string;
    unitCd: string;
    startDtm?: string;
    endDtm?: string;
  }): Promise<HeatmapCount[]> => {
    const { data } = await axios.get(`${BASE}/event/heatmap/count`, { params });
    return data;
  },

  /** 상황전파 이벤트 목록 */
  fetchDstrEvents: async (params: { rows?: number; startDtm?: string; endDtm?: string }): Promise<DstrInfo[]> => {
    const { data } = await axios.get(`${BASE}/event/dstr`, { params });
    return data;
  },

  /** 상황전파 단건 */
  fetchDstrEvent: async (uSvcOutbId: string): Promise<DstrInfo> => {
    const { data } = await axios.get(`${BASE}/event/dstr/${uSvcOutbId}`);
    return data;
  },

  /** 소방차 출동 정보 (표준시스템 OCS) */
  fetchFireCar: async (statEvetOutbSeqn: string): Promise<FireCarInfo[]> => {
    const { data } = await axios.get(`${BASE}/standard/system`, {
      params: { statEvetOutbSeqn },
    });
    return data ?? [];
  },

  /** 이벤트 종료 */
  closeEvent: async (statEvetOutbSeqn: string): Promise<{ result: string }> => {
    const { data } = await axios.post(`${BASE}/event/close`, null, {
      params: { statEvetOutbSeqn },
    });
    return data;
  },
};
