export const DND_ITEM_TYPE = {
  CCTV: 'CCTV',
  CLFY: 'CLFY',
} as const;

export interface DndCctvItem {
  type: typeof DND_ITEM_TYPE.CCTV;
  cctv: import('./types').CctvInfo;
}

export interface DndClfyItem {
  type: typeof DND_ITEM_TYPE.CLFY;
  cctvList: import('./types').CctvInfo[];
  clfyId: string;
}

export type DndItem = DndCctvItem | DndClfyItem;
