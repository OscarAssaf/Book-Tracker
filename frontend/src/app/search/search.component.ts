import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MediaService } from '../services/media.service';
import { ALL_TYPES, MediaItemRequest, MediaType, SearchResult, TYPE_LABELS } from '../models/media.model';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [FormsModule],
  template: `
    <form class="search" (ngSubmit)="search()">
      <input name="q" [(ngModel)]="query" placeholder="Search books, manga, manhwa, anime…" />
      <select name="type" [(ngModel)]="type">
        <option value="">All types</option>
        @for (t of types; track t) {
          <option [value]="t">{{ labels[t] }}</option>
        }
      </select>
      <button type="submit" [disabled]="loading()">Search</button>
    </form>

    @if (message()) { <p class="message">{{ message() }}</p> }
    @if (loading()) { <p>Searching…</p> }
    @if (!searched()) {
      <p class="empty">Search for something to add to your library.</p>
    }
    @if (searched() && !loading() && results().length === 0) {
      <p class="empty">No results found. Try a different title or type.</p>
    }

    <div class="grid">
      @for (r of results(); track key(r)) {
        <article class="card">
          @if (r.coverUrl) {
            <img [src]="r.coverUrl" [alt]="r.title" />
          }
          <div class="body">
            <h3>{{ r.title }}</h3>
            <p class="meta">
              {{ labels[r.type] }}
              @if (r.creators) { · {{ r.creators }} }
              @if (r.totalUnits) { · {{ r.totalUnits }} units }
            </p>
            @if (r.description) {
              <p class="desc">{{ r.description }}</p>
            }
            <button [disabled]="isAdded(r)" (click)="add(r)">
              {{ isAdded(r) ? 'Added' : 'Add to library' }}
            </button>
          </div>
        </article>
      }
    </div>
  `,
})
export class SearchComponent {
  private media = inject(MediaService);

  readonly labels = TYPE_LABELS;
  readonly types = ALL_TYPES;

  query = '';
  type: MediaType | '' = '';

  results = signal<SearchResult[]>([]);
  addedKeys = signal<string[]>([]);
  loading = signal(false);
  searched = signal(false);
  message = signal<string | null>(null);

  key(r: SearchResult): string {
    return `${r.source}:${r.externalId}`;
  }

  isAdded(r: SearchResult): boolean {
    return this.addedKeys().includes(this.key(r));
  }

  search(): void {
    const q = this.query.trim();
    if (!q) return;
    this.loading.set(true);
    this.message.set(null);
    this.media.search(q, this.type).subscribe({
      next: results => {
        this.results.set(results);
        this.searched.set(true);
        this.loading.set(false);
      },
      error: () => {
        this.message.set('Search failed. Is the backend running on port 8080?');
        this.loading.set(false);
      },
    });
  }

  add(r: SearchResult): void {
    const request: MediaItemRequest = {
      title: r.title,
      creators: r.creators,
      type: r.type,
      status: 'PLANNED',
      coverUrl: r.coverUrl,
      description: r.description,
      totalUnits: r.totalUnits,
      progress: 0,
      externalSource: r.source,
      externalId: r.externalId,
    };
    this.media.create(request).subscribe({
      next: () => {
        this.markAdded(r);
        this.message.set(`Added "${r.title}" to your library.`);
      },
      error: err => {
        if (err.status === 409) {
          this.markAdded(r);
          this.message.set(`"${r.title}" is already in your library.`);
        } else {
          this.message.set('Could not add the item. Please try again.');
        }
      },
    });
  }

  private markAdded(r: SearchResult): void {
    this.addedKeys.update(keys => [...keys, this.key(r)]);
  }
}