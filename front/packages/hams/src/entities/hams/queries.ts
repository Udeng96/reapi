import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  fetchHamsList,
  fetchHamsDetail,
  fetchCctvList,
  fetchDoorStat,
  lockDoor,
  unlockDoor,
} from './api';
import type { StatOptions } from './types';

export function useHamsList() {
  return useQuery({
    queryKey: ['hams', 'list'],
    queryFn: fetchHamsList,
  });
}

export function useHamsDetail(id: string | null) {
  return useQuery({
    queryKey: ['hams', 'detail', id],
    queryFn: () => fetchHamsDetail(id!),
    enabled: !!id,
  });
}

export function useHamsCctvList(hamsName: string | null) {
  return useQuery({
    queryKey: ['hams', 'cctv', hamsName],
    queryFn: () => fetchCctvList(hamsName!),
    enabled: !!hamsName,
  });
}

export function useHamsDoorStat(hamsId: string | null, options: StatOptions) {
  return useQuery({
    queryKey: ['hams', 'stat', hamsId, options],
    queryFn: () => fetchDoorStat(hamsId!, options),
    enabled: !!hamsId,
  });
}

export function useLockDoor() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ hamsId, body }: { hamsId: string; body: Parameters<typeof lockDoor>[1] }) =>
      lockDoor(hamsId, body),
    onSuccess: (_, { hamsId }) => {
      qc.invalidateQueries({ queryKey: ['hams', 'detail', hamsId] });
    },
  });
}

export function useUnlockDoor() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ hamsId, body }: { hamsId: string; body: Parameters<typeof unlockDoor>[1] }) =>
      unlockDoor(hamsId, body),
    onSuccess: (_, { hamsId }) => {
      qc.invalidateQueries({ queryKey: ['hams', 'detail', hamsId] });
    },
  });
}
