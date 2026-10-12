import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'library', pathMatch: 'full' },
  {
    path: 'library',
    loadComponent: () => import('./library/library.component').then(m => m.LibraryComponent),
  },
  {
    path: 'search',
    loadComponent: () => import('./search/search.component').then(m => m.SearchComponent),
  },
  { path: '**', redirectTo: 'library' },
];