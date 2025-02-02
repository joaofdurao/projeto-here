import { Router } from '@angular/router';
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { LoginForm } from './login-form';
import { LoginRepository } from '../../repository/login.repository';
import { AuthService } from '../../core/components/infra/auth.service';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputTextModule, ButtonModule, FloatLabelModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  public loginForm = inject(LoginForm)
  private loginRepository = inject(LoginRepository)
  private authService = inject(AuthService)

  constructor(private router: Router) {}

  onSubmit() {
    if (this.loginForm.valid()) {
      this.loginRepository.login(this.loginForm.value).subscribe(value =>{
        if (!value.error) {
          this.loginForm.reset()
          this.authService.login(value.data)
          this.router.navigate(["/home"])
          }
 }
      )
    }
  }
  public onCreateAccount() {
    this.router.navigate(["/new-user"])
  }

}
