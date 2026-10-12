import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MediaService } from '../services/media.service';
import {
  ALL_STATUSES, ALL_TYPES, MediaItem, MediaType, ReadingStatus, STATUS_LABELS, TYPE_LABELS,
} from '../models/media.model';

@Component({
  selector: 'app-library',
  standalone: true,
  imports: [FormsModule],
  template: `
    <section class="toolbar">
      <select [(ngModel)]="typeFilter" (ngModelChange)="load()">
        <option value="">All types</option>
        @for (t of types; track t) {
          <option [value]="t">{{ labels[t] }}</option>
        }
      </select>
      <select [(ngModel)]="statusFilter" (ngModelChange)="load()">
        <option value="">All statuses</option>
        @for (s of statuses; track s) {
          <option [value]="s">{{ statusLabels[s] }}</option>
        }
      </select>
    </section>

    @if (error()) { <p class="error">{{ error() }}</p> }
    @if (loading()) { <p>Loading…</p> }
    @if (!loading() && items().length === 0) {
      <p class="empty">Nothing here yet. Use "Search &amp; add" to get started.</p>
    }

    <div class="grid">
      @for (item of items(); track item.id) {
        <article class="card">
          @if (item.coverUrl) {
            <img [src]="item.coverUrl" [alt]="item.title" />
          }
          <div class="body">
            <h3>{{ item.title }}</h3>
            <p class="meta">
              {{ labels[item.type] }}
              @if (item.creators) { · {{ item.creators }} }
            </p>
            <p class="meta">
              {{ statusLabels[item.status] }} · {{ item.progress }}
              @if (item.totalUnits) { / {{ item.totalUnits }} }
            </p>
          </div>
        </article>
      }
    </div>
  `,
})
export class LibraryComponent implements OnInit {
  private media = inject(MediaService);

  readonly labels = TYPE_LABELS;
  readonly statusLabels = STATUS_LABELS;
  readonly types = ALL_TYPES;
  readonly statuses = ALL_STATUSES;

  typeFilter: MediaType | '' = '';
  statusFilter: ReadingStatus | '' = '';

  items = signal<MediaItem[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.media.list({ type: this.typeFilter, status: this.statusFilter }).subscribe({
      next: items => {
        this.items.set(items);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load your library. Is the backend running on port 8080?');
        this.loading.set(false);
      },
    });
  }
}