import { Component, LOCALE_ID, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./core/components/header/header.component";
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

registerLocaleData(localePt);

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [{ provide: LOCALE_ID, useValue: 'pt-BR' }]
})
export class AppComponent implements OnInit {
  currentRoute: string = '';

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {}

   ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.urlAfterRedirects;
      }
    });
  }

  isLoginPage() {
    return this.currentRoute === '/login' || this.currentRoute === '/new-user'? true : false
  }
}
