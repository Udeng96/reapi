import { useQuery } from '@tanstack/react-query';
import { eventApi } from './api';
import type { EventsPayload } from './types';

export const eventKeys = {
  all: ['event'] as const,
  lists: () => [...eventKeys.all, 'list'] as const,
  list: (payload: EventsPayload) => [...eventKeys.lists(), payload] as const,
  details: () => [...eventKeys.all, 'detail'] as const,
  detail: (id: string) => [...eventKeys.details(), id] as const,
  types: () => [...eventKeys.all, 'types'] as const,
  locations: () => [...eventKeys.all, 'locations'] as const,
  heatmapClfy: () => [...eventKeys.all, 'heatmap', 'clfy'] as const,
  heatmapCount: (params: object) => [...eventKeys.all, 'heatmap', 'count', params] as const,
  dstrEvents: (params: object) => [...eventKeys.all, 'dstr', params] as const,
  fireCar: (seqn: string) => [...eventKeys.all, 'fireCar', seqn] as const,
};

const MOCK_EVENTS = {
  total: 5,
  data: [
    { statEvetOutbSeqn: 'mock-1', usvcOutbId: 'u1', locationCd: 'loc1', locationNm: '향남읍', eventId: 'evt1', eventNm: '5대연계 112', addr: '경기도 화성시 향남읍 발안로 123', outbDtm: '20260403140000', clsDtm: null, lat: '37.18', lng: '126.98', cntn: '112 신고 접수 내용입니다.' },
    { statEvetOutbSeqn: 'mock-2', usvcOutbId: 'u2', locationCd: 'loc2', locationNm: '정남면', eventId: 'evt1', eventNm: '5대연계 112', addr: '경기도 화성시 정남면 발안로 456', outbDtm: '20260403135600', clsDtm: null, lat: '37.21', lng: '127.03', cntn: '정남면 112 신고입니다.' },
    { statEvetOutbSeqn: 'mock-3', usvcOutbId: 'u3', locationCd: 'loc3', locationNm: '세출동', eventId: 'evt2', eventNm: '5대연계 소방', addr: '경기도 화성시 세출동 수노을2로 789', outbDtm: '20260403134900', clsDtm: null, lat: '37.15', lng: '126.95', cntn: '소방 출동 요청입니다.' },
    { statEvetOutbSeqn: 'mock-4', usvcOutbId: 'u4', locationCd: 'loc4', locationNm: '반월동', eventId: 'evt1', eventNm: '5대연계 112', addr: '경기도 화성시 반월동 동단원전로 101', outbDtm: '20260403133100', clsDtm: null, lat: '37.22', lng: '127.01', cntn: '반월동 신고 내용.' },
    { statEvetOutbSeqn: 'mock-5', usvcOutbId: 'u5', locationCd: 'loc1', locationNm: '향남읍', eventId: 'evt3', eventNm: '5대연계 경찰', addr: '경기도 화성시 향남읍 만세로 구 플랫폼 이벤트 발생 지점', outbDtm: '20260403131300', clsDtm: null, lat: '37.19', lng: '126.99', cntn: '경찰 신고 접수. 현장 출동 요청합니다. 자세한 내용은 추후 업데이트 예정.' },
  ],
};

export const useEvents = (payload: EventsPayload, enabled = true) =>
  useQuery({
    queryKey: eventKeys.list(payload),
    queryFn: import.meta.env.DEV
      ? () => Promise.resolve(MOCK_EVENTS)
      : () => eventApi.fetchEvents(payload),
    enabled,
  });

export const useEventDetail = (id: string | null) =>
  useQuery({
    queryKey: eventKeys.detail(id ?? ''),
    queryFn: import.meta.env.DEV
      ? () => Promise.resolve(MOCK_EVENTS.data.find((e) => e.statEvetOutbSeqn === id) ?? null)
      : () => eventApi.fetchEvent(id!),
    enabled: !!id,
  });

export const useEventTypes = () =>
  useQuery({
    queryKey: eventKeys.types(),
    queryFn: eventApi.fetchEventTypes,
    staleTime: Infinity,
  });

export const useLocations = () =>
  useQuery({
    queryKey: eventKeys.locations(),
    queryFn: eventApi.fetchLocations,
    staleTime: Infinity,
  });

export const useHeatmapClfy = () =>
  useQuery({
    queryKey: eventKeys.heatmapClfy(),
    queryFn: eventApi.fetchHeatmapClfy,
    staleTime: Infinity,
  });

export const useHeatmapCount = (
  params: { svcThemeCd: string; unitCd: string; startDtm?: string; endDtm?: string },
  enabled = true,
) =>
  useQuery({
    queryKey: eventKeys.heatmapCount(params),
    queryFn: () => eventApi.fetchHeatmapCount(params),
    enabled,
  });

export const useDstrEvents = (
  params: { rows?: number; startDtm?: string; endDtm?: string } = {},
) =>
  useQuery({
    queryKey: eventKeys.dstrEvents(params),
    queryFn: () => eventApi.fetchDstrEvents(params),
  });

export const useFireCar = (statEvetOutbSeqn: string | null) =>
  useQuery({
    queryKey: eventKeys.fireCar(statEvetOutbSeqn ?? ''),
    queryFn: () => eventApi.fetchFireCar(statEvetOutbSeqn!),
    enabled: !!statEvetOutbSeqn,
  });
