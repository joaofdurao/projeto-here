import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class UserDetailForm {
  public userForm: FormGroup;
  initialFormValues: any

  constructor(private formBuilder: FormBuilder) {
    this.userForm = this.formBuilder.group({
      name: [''],
      course: [''],
      email: ['', Validators.email],
      password: [''],
      confirmPassword: [''],
      registration: ['']
  });
  }

  reset() {
    this.userForm.reset();
  }

  get value() {
    return { ...this.userForm.value };
  }

  valid() {
    Object.values(this.userForm.controls).forEach(control => {
      control.markAsDirty();
      control.markAsTouched();
    });
    return this.userForm.valid;
  }

  public get userFormGroup() {
    return this.userForm
  }

  public get name() {
    return this.userForm.get('name');
  }

  public get course() {
    return this.userForm.get('course');
  }

  public get email() {
    return this.userForm.get('email');
  }

  public get password() {
    return this.userForm.get('password');
  }

  public get confirmPassword() {
    return this.userForm.get('confirmPassword');
  }

}
