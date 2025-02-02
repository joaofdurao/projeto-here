import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { UserRepository } from '../../../repository/user.repository';
import { User } from '../../../domain/user';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { UserDetailForm } from './user-detail-form';

@Component({
  standalone: true,
  selector: 'app-user-form',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css'],
  imports: [CommonModule, ReactiveFormsModule, ButtonModule],
})
export class UserDetailComponent implements OnInit {
  public user: User
  public isEditing = false;

  constructor(
  public userDetailForm: UserDetailForm,
  private userRepository: UserRepository
  ) {}

  ngOnInit() {
    this.userRepository.getUserDetail().subscribe(
      (value) => {
        if (!value.error) {
          this.user = value.data
          this.userDetailForm.userForm.patchValue({...this.user})
        }
    },
    );
  };

  public onSave() {
    if (this.userDetailForm.valid) {
      const updatedDetails: User = {...this.modifiedValues()}

      this.userRepository.updateUser(updatedDetails).subscribe(
        (value) => {
        if (!value.error) {
          this.user = value.data
          this.userDetailForm.userForm.patchValue({...this.user})
        }
      }
      );
    }
  }

  public get isPasswordEquals() {
    if (this.userDetailForm.password?.touched && this.userDetailForm.confirmPassword?.touched) {
      return this.userDetailForm.password.value === this.userDetailForm.confirmPassword.value
    }
    return true
}


public modifiedValues() {
  const inicialValues = this.user
  const currentValues = this.userDetailForm.value;
  const modifiedValues: any = {}

  Object.keys(currentValues).forEach((key) => {
    if (key !== "confirmPassword") {
      if (currentValues[key] !== inicialValues[key]) {
        modifiedValues[key] = currentValues[key];
      }
    }
  });

  return modifiedValues;
}
}
