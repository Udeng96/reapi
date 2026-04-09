import { useQuery } from '@tanstack/react-query';
import { cctvApi } from './api';
import type { CctvSearchOption } from './types';

export const cctvKeys = {
  all: ['cctv'] as const,
  lists: () => [...cctvKeys.all, 'list'] as const,
  list: (option: CctvSearchOption) => [...cctvKeys.lists(), option] as const,
  allForGis: () => [...cctvKeys.all, 'gis'] as const,
  details: () => [...cctvKeys.all, 'detail'] as const,
  detail: (id: string) => [...cctvKeys.details(), id] as const,
  clfy: () => [...cctvKeys.all, 'clfy'] as const,
};

export const useCctvList = (option: CctvSearchOption, enabled = true) =>
  useQuery({
    queryKey: cctvKeys.list(option),
    queryFn: () => cctvApi.fetchList(option),
    enabled,
    placeholderData: (prev) => prev,
  });

export const useCctvAllForGis = () =>
  useQuery({
    queryKey: cctvKeys.allForGis(),
    queryFn: cctvApi.fetchAllForGis,
    staleTime: 1000 * 60 * 5, // 5분
  });

export const useCctvDetail = (facId: string | null) =>
  useQuery({
    queryKey: cctvKeys.detail(facId ?? ''),
    queryFn: () => cctvApi.fetchDetail(facId!),
    enabled: !!facId,
  });

export const useCctvClfy = () =>
  useQuery({
    queryKey: cctvKeys.clfy(),
    queryFn: cctvApi.fetchClfy,
    staleTime: Infinity,
  });
