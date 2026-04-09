import axios from 'axios';
import type { MobileInfo, MobilePaging, MobileSearchPayload } from './types';

const BASE = '/api';

export const mobileApi = {
  /** 이동수단 현재 위치 목록 (실시간) */
  fetchLive: async (): Promise<MobileInfo[]> => {
    const { data } = await axios.get(`${BASE}/mobile/live`);
    return data ?? [];
  },

  /** 이동수단 과거 이력 (페이징) */
  fetchPast: async (payload: MobileSearchPayload): Promise<MobilePaging<MobileInfo>> => {
    const { data } = await axios.get(`${BASE}/mobile/past`, { params: payload });
    return data ?? { total: 0, data: [] };
  },
};
