import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  template: `
    <header class="topbar">
      <span class="brand"> Book Tracker</span>
      <nav>
        <a routerLink="/library" routerLinkActive="active">My library</a>
        <a routerLink="/search" routerLinkActive="active">Search &amp; add</a>
      </nav>
    </header>
    <main>
      <router-outlet />
    </main>
  `,
})
export class AppComponent {}