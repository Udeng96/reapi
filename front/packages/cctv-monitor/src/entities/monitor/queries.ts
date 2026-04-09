import { useQuery, useMutation } from '@tanstack/react-query';
import { fetchMonitorList, fetchFirstMonitorPage, controlCctv } from './api';
import type { CctvControlParam } from './types';

export function useMonitorList() {
  return useQuery({
    queryKey: ['monitor', 'list'],
    queryFn: fetchMonitorList,
  });
}

export function useFirstMonitorPage(clfyId: string | null) {
  return useQuery({
    queryKey: ['monitor', 'firstPage', clfyId],
    queryFn: () => fetchFirstMonitorPage(clfyId!),
    enabled: !!clfyId,
  });
}

export function useCctvControl() {
  return useMutation({
    mutationFn: (param: CctvControlParam) => controlCctv(param),
  });
}
