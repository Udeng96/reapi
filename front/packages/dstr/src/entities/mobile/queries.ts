import { useQuery } from '@tanstack/react-query';
import { mobileApi } from './api';
import type { MobileSearchPayload } from './types';

export const mobileKeys = {
  all: ['mobile'] as const,
  live: () => [...mobileKeys.all, 'live'] as const,
  pasts: () => [...mobileKeys.all, 'past'] as const,
  past: (payload: MobileSearchPayload) => [...mobileKeys.pasts(), payload] as const,
};

export const useMobileLive = (enabled = true) =>
  useQuery({
    queryKey: mobileKeys.live(),
    queryFn: mobileApi.fetchLive,
    refetchInterval: 10_000, // 10초마다 갱신
    enabled,
  });

export const useMobilePast = (payload: MobileSearchPayload, enabled = true) =>
  useQuery({
    queryKey: mobileKeys.past(payload),
    queryFn: () => mobileApi.fetchPast(payload),
    enabled,
  });
