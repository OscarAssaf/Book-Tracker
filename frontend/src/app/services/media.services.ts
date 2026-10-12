import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { MediaItem, MediaItemRequest, MediaType, ReadingStatus, SearchResult } from '../models/media.model';

@Injectable({ providedIn: 'root' })
export class MediaService {
  private http = inject(HttpClient);
  private readonly base = 'http://localhost:8080/api';

  list(filters: { type?: MediaType | ''; status?: ReadingStatus | '' } = {}): Observable<MediaItem[]> {
    let params = new HttpParams();
    if (filters.type) params = params.set('type', filters.type);
    if (filters.status) params = params.set('status', filters.status);
    return this.http.get<MediaItem[]>(`${this.base}/items`, { params });
  }

  create(request: MediaItemRequest): Observable<MediaItem> {
    return this.http.post<MediaItem>(`${this.base}/items`, request);
  }

  update(id: number, request: MediaItemRequest): Observable<MediaItem> {
    return this.http.put<MediaItem>(`${this.base}/items/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/items/${id}`);
  }

  search(query: string, type: MediaType | ''): Observable<SearchResult[]> {
    let params = new HttpParams().set('q', query);
    if (type) params = params.set('type', type);
    return this.http.get<SearchResult[]>(`${this.base}/search`, { params });
  }
}