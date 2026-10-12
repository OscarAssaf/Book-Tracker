export type MediaType = 'BOOK' | 'MANGA' | 'MANHWA' | 'ANIME';
export type ReadingStatus = 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'DROPPED';

export interface MediaItem {
  id: number;
  title: string;
  creators: string | null;
  type: MediaType;
  status: ReadingStatus;
  coverUrl: string | null;
  description: string | null;
  externalSource: string | null;
  externalId: string | null;
  totalUnits: number | null;
  progress: number;
  rating: number | null;
  notes: string | null;
  addedAt: string;
  updatedAt: string;
}

export interface MediaItemRequest {
  title: string;
  creators?: string | null;
  type: MediaType;
  status: ReadingStatus;
  coverUrl?: string | null;
  description?: string | null;
  totalUnits?: number | null;
  progress?: number | null;
  rating?: number | null;
  notes?: string | null;
  externalSource?: string | null;
  externalId?: string | null;
}

export interface SearchResult {
  source: string;
  externalId: string;
  title: string;
  creators: string | null;
  type: MediaType;
  coverUrl: string | null;
  description: string | null;
  totalUnits: number | null;
}

export const ALL_TYPES: MediaType[] = ['BOOK', 'MANGA', 'MANHWA', 'ANIME'];
export const ALL_STATUSES: ReadingStatus[] = ['PLANNED', 'IN_PROGRESS', 'COMPLETED', 'DROPPED'];

export const TYPE_LABELS: Record<MediaType, string> = {
  BOOK: 'Book',
  MANGA: 'Manga',
  MANHWA: 'Manhwa',
  ANIME: 'Anime',
};

export const STATUS_LABELS: Record<ReadingStatus, string> = {
  PLANNED: 'Planned',
  IN_PROGRESS: 'In progress',
  COMPLETED: 'Completed',
  DROPPED: 'Dropped',
};

export function toRequest(item: MediaItem): MediaItemRequest {
  return {
    title: item.title,
    creators: item.creators,
    type: item.type,
    status: item.status,
    coverUrl: item.coverUrl,
    description: item.description,
    totalUnits: item.totalUnits,
    progress: item.progress,
    rating: item.rating,
    notes: item.notes,
    externalSource: item.externalSource,
    externalId: item.externalId,
  };
}