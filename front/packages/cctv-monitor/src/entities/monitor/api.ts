import { apiClient } from "@reapi/shared";
import type { CctvControlParam, MenuListResult } from "./types";

const BASE_URL = "/api";

export async function fetchMonitorList(): Promise<MenuListResult[]> {
  const { data } = await apiClient.get(`${BASE_URL}/cctv/monitor`);
  return data;
}

export async function fetchFirstMonitorPage(clfyId: string): Promise<MenuListResult[]> {
  const { data } = await apiClient.get(`${BASE_URL}/cctv/monitor?clfyId=${clfyId}`);
  return data;
}

export async function controlCctv(param: CctvControlParam): Promise<string> {
  const { data } = await apiClient.post(`${BASE_URL}/cctv/control/${param.cctvId}`, param);
  return data;
}
