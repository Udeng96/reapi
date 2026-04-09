import { apiClient } from '@reapi/shared';
import type { HamsGisFacInfo, HamsCctvInfo, HamsDoorStat, LockResult, StatOptions } from './types';

const BASE = '/api/hams/api';

/** BE DTO → FE 타입 변환 (name→facNm, address→addr) */
function mapDto(item: any): HamsGisFacInfo {
  return {
    ...item,
    facNm: item.facNm ?? item.name ?? null,
    addr: item.addr ?? item.address ?? null,
    port: item.port != null ? String(item.port) : null,
  };
}

/** 국사 목록 전체 조회 */
export async function fetchHamsList(): Promise<HamsGisFacInfo[]> {
  const { data } = await apiClient.get<any[]>(BASE);
  return data.map(mapDto);
}

/** 국사 단건 상세 조회 */
export async function fetchHamsDetail(id: string): Promise<HamsGisFacInfo> {
  const { data } = await apiClient.get<any>(`${BASE}/${id}`);
  return mapDto(data);
}

/** CCTV 목록 조회 */
export async function fetchCctvList(hamsName: string): Promise<HamsCctvInfo[]> {
  const { data } = await apiClient.get<HamsCctvInfo[]>(`${BASE}/cctv`, {
    params: { hamsName },
  });
  return data;
}

/** 도어 개폐 통계 조회 */
export async function fetchDoorStat(
  hamsId: string,
  options: StatOptions
): Promise<HamsDoorStat[]> {
  const { data } = await apiClient.get<HamsDoorStat[]>(`${BASE}/${hamsId}/stat`, {
    params: options,
  });
  return data;
}

/** 도어락 잠금 */
export async function lockDoor(
  hamsId: string,
  body: HamsGisFacInfo | null
): Promise<LockResult> {
  const { data } = await apiClient.post<LockResult>(`${BASE}/${hamsId}/lock`, body);
  return data;
}

/** 도어락 잠금해제 */
export async function unlockDoor(
  hamsId: string,
  body: HamsGisFacInfo | null
): Promise<LockResult> {
  const { data } = await apiClient.post<LockResult>(`${BASE}/${hamsId}/unlock`, body);
  return data;
}
