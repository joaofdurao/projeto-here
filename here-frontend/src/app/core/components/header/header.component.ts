import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { TabMenuModule } from 'primeng/tabmenu';
import { LoginRepository } from '../../../repository/login.repository';
import { AuthService } from '../infra/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [TabMenuModule, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  public items!: MenuItem[];
  activeItem: MenuItem
  private loginRepository = inject(LoginRepository)
  private authService = inject(AuthService)

  constructor(private route: Router) {}

  ngOnInit() {
      this.items = [
          { label: 'Home', icon: '', route: '/home' },
          { label: 'Meus eventos', icon: '', route: '/my-events' },
          { label: 'Perfil', icon: '', route: '/user' },
          { label: 'Logout', icon: '', command: () => {
                this.authService.logout()
                this.route.navigate(["/login"])
        } }
      ]
      this.activeItem = this.items[0];
    }
}
