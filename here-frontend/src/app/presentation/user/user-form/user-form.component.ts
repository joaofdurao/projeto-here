import { CadastroForm } from './cadastro-form';
import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { UserRepository } from '../../../repository/user.repository';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { User } from '../../../domain/user';
import { LoginRepository } from '../../../repository/login.repository';
import { Login } from '../../../domain/login';
import { AuthService } from '../../../core/components/infra/auth.service';

@Component({
  standalone: true,
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
  imports: [ReactiveFormsModule, CommonModule, ButtonModule],
})
export class UserFormComponent implements OnInit {
  private user = new User()
  private login = new Login()
  private authService = inject(AuthService)
  private userRepository = inject(UserRepository)
  private loginRepository = inject(LoginRepository)

  constructor(
    public createUserForm: CadastroForm,
    private router: Router) {}

  ngOnInit() {}

  public onSubmit() {
    if (this.createUserForm.valid()) {
      this.getRegistration();
      this.userRepository.createUser(this.user).subscribe(value => {
        if (!value.error) {
          this.login.email = this.createUserForm.email.value
          this.login.password = this.createUserForm.password.value
          this.loginRepository.login(this.login).subscribe(value => {
            if (!value.error) {
            this.authService.login(value.data)
            this.router.navigate(["/home"])
            this.createUserForm.reset()
            }
          })
        }
      })
    }
  }

    private getRegistration() {
      this.user = this.createUserForm.value
      const emailValue = this.createUserForm.email?.value

      if(emailValue){
        const registrationMatch = emailValue.match(/(\d+)(?=@)/);
        if (registrationMatch) {
          this.user.registration = registrationMatch[0]
        }
      }
    }

    public get isPasswordEquals() {
      if (this.createUserForm.password?.touched && this.createUserForm.confirmPassword?.touched) {
        return this.createUserForm.password.value === this.createUserForm.confirmPassword.value
      }
      return true
  }

    public onCancel() {
      this.router.navigate(["/login"])
    }

}
