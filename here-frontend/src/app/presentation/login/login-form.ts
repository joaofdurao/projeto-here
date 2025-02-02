import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Login } from '../../domain/login';

@Injectable({providedIn: 'root'})
export class LoginForm {
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      email: this.formBuilder.control('', [Validators.required, Validators.email]),
      password: this.formBuilder.control('', Validators.required)
    })
  }

  reset() {
    this.loginForm.reset()
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get value(): Login {
    return { ...this.loginForm.value } as Login
  }

  valid() {
    Object.values(this.loginForm.controls).forEach(control => {
      control.markAsDirty()
      control.markAsTouched()
    })
    return this.loginForm.valid
  }
}
