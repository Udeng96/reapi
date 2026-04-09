import type { CctvInfo, MenuListResult } from './types';

/**
 * CCTV 분류 목록을 그리드 페이지 배열로 변환
 * LCM(최소공배수) 기반으로 모든 분류의 CCTV가 균등하게 순환
 */
export function getAllPagePlayList(
  menuList: MenuListResult[],
  gridAmount: number,
): (CctvInfo | null)[][] {
  if (!menuList || menuList.length === 0 || gridAmount <= 0) return [];

  // 전체 CCTV를 flat하게 합침
  const allCctvList: CctvInfo[] = menuList.flatMap((m) => m.cctvList);
  if (allCctvList.length === 0) return [];

  // 페이지 단위로 분할
  const pages: (CctvInfo | null)[][] = [];
  for (let i = 0; i < allCctvList.length; i += gridAmount) {
    const page: (CctvInfo | null)[] = Array(gridAmount).fill(null);
    const slice = allCctvList.slice(i, i + gridAmount);
    slice.forEach((cctv, idx) => {
      page[idx] = cctv;
    });
    pages.push(page);
  }

  return pages;
}

/**
 * 특정 분류의 CCTV를 페이지 배열로 변환
 */
export function getClfyPagePlayList(
  cctvList: CctvInfo[],
  gridAmount: number,
): (CctvInfo | null)[][] {
  if (!cctvList || cctvList.length === 0 || gridAmount <= 0) return [];

  const pages: (CctvInfo | null)[][] = [];
  for (let i = 0; i < cctvList.length; i += gridAmount) {
    const page: (CctvInfo | null)[] = Array(gridAmount).fill(null);
    const slice = cctvList.slice(i, i + gridAmount);
    slice.forEach((cctv, idx) => {
      page[idx] = cctv;
    });
    pages.push(page);
  }

  return pages;
}
