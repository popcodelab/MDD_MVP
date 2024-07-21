import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {MatButton} from "@angular/material/button";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [
    MatFormField,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    MatButton,
    NgIf
  ],
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss'
})
export class MeComponent implements OnInit, OnDestroy{


  formControls: { [key: string]: FormControl } = {
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4)]),
    email: new FormControl('', [
      Validators.email,
      Validators.required]),
  };

  controlNames: { [key: string]: string } = {
    username: APP_CONSTANTS.VALIDATION_MESSAGES.USERNAME_VALIDATION_RULE,
    email: APP_CONSTANTS.VALIDATION_MESSAGES.EMAIL_VALIDATION_RULE
  };

  labels: { [key: string]: string } = {
    username: 'Username',
    email: 'Email',
  };

  errorMessages: { [key: string]: string } = {
    username: '',
    email: '',
  };

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  onSubmit(): void {}

  onBlur(controlName: string): void {}

}
