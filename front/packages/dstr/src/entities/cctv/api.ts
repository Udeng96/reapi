import axios from 'axios';
import type { CctvInfo, CctvClfy, CctvListResult, CctvSearchOption } from './types';

const BASE = '/api';

export const cctvApi = {
  /** CCTV 목록 (페이징) */
  fetchList: async (option: CctvSearchOption): Promise<CctvListResult> => {
    const { data } = await axios.get(`${BASE}/cctv`, { params: option });
    return { total: data.total, data: data.list ?? null };
  },

  /** CCTV 전체 목록 (GIS 마커용 — 페이징 없이 전체) */
  fetchAllForGis: async (): Promise<CctvInfo[]> => {
    const { data } = await axios.get(`${BASE}/cctv`, {
      params: { page: 1, rows: 9999 },
    });
    return (data?.list ?? []).filter((item: CctvInfo) => item.lat !== null && item.lng !== null);
  },

  /** CCTV 단건 조회 */
  fetchDetail: async (facId: string): Promise<CctvInfo> => {
    const { data } = await axios.get(`${BASE}/cctv/${facId}`);
    return data;
  },

  /** CCTV 분류 목록 */
  fetchClfy: async (): Promise<CctvClfy[]> => {
    const { data } = await axios.get(`${BASE}/cctv/clfy`);
    return data;
  },
};
