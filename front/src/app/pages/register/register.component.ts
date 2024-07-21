import {Component, OnDestroy} from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MaterialModule} from "../../shared/material.modules";
import {Subscription} from "rxjs";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    MaterialModule,
    NgClass,
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
/**
 * Represents the RegisterComponent class.
 *
 * This class is responsible for handling the registration functionality of the application.
 * It implements the OnInit and OnDestroy interfaces.
 *
 * @class
 * @implements {OnDestroy}
 *
 * @author Pignon Pierre-Olivier
 *
 * @version 1.0
 */
export class RegisterComponent implements OnDestroy {

  private readonly PASSWORD_VALIDATION_RULE: string = 'a password with at least 8 characters, including 1 uppercase letter, ' +
    '1 lowercase letter, 1 number and 1 special character';
  private readonly USERNAME_VALIDATION_RULE: string = 'a username with at least 4 characters';

  private readonly EMAIL_VALIDATION_RULE: string = "a valid email address"

  // AuthService subscription
  private authServiceSubscription: Subscription | undefined;

  // Object that contains form control objects.
  formControls: { [key: string]: FormControl } = {
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4)]),
    email: new FormControl('', [
      Validators.email,
      Validators.required]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$'),
    ]),
  };


  /**
   * Control names variable.
   *
   * @description An object that maps control names to their corresponding descriptions.
   *              Each control name is a string key associated with a string value.
   *              The string value describes the requirements or purpose of the control.
   *
   * @property {string} username - The description of the username control. It should have at least 4 characters.
   * @property {string} email - The description of the email control. It should be a valid email address.
   * @property {string} password - The description of the password control. It should have at least 8 characters,
   *                              including at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character.
   */
  controlNames: { [key: string]: string } = {
    username: this.USERNAME_VALIDATION_RULE,
    email: this.EMAIL_VALIDATION_RULE,
    password: this.PASSWORD_VALIDATION_RULE,
  };

  /**
   * An object that contains the labels for different variables.
   *
   * @property {string} username - The label for a username variable.
   * @property {string} email - The label for an email variable.
   * @property {string} password - The label for a password variable.
   */
  labels: { [key: string]: string } = {
    username: 'Username',
    email: 'Email',
    password: 'Password',
  };

  /**
   * @description
   * The `errorMessages` variable is an object that stores error messages for different keys.
   * The object follows the structure where each key represents a field identifier and its corresponding value
   * represents the error message associated with that field.
   *
   * @property {string} [username=''] The error message for the username field.
   * @property {string} [email=''] The error message for the email field.
   * @property {string} [password=''] The error message for the password field.
   */
  errorMessages: { [key: string]: string } = {
    username: '',
    email: '',
    password: '',
  };

  onSubmit(): void {
    if (this.formControls["username"].valid && this.formControls['email'].valid && this.formControls['password'].valid) {
      // const registerRequest: RegisterRequest = {
      //   username: this.formControls['username'].value,
      //   email: this.formControls['email'].value,
      //   password: this.formControls['password'].value,
      // };
      // this.authServiceSubscription = this.authService.register(registerRequest).subscribe({
      //   next: () => {
      //     this.router.navigate(['/posts']).then(
      //       () => {}
      //     );
      //   },
      //   error: error => {
      //     throw error;
      //   }
      // });
    }}

  /**
   * Function to handle onBlur event of form control
   * When leave a control, check if there is some errors on the form
   * @param {string} controlName - Name of the form control
   * @returns {void}
   */
  onBlur(controlName: string): void {
    const control: FormControl<any> = this.formControls[controlName];
    control.markAsTouched();
    if (control.hasError('required')) {
      this.errorMessages[controlName] = `Please enter ${this.controlNames[controlName]}`;
    } else if (control.hasError('minlength')) {
      if (controlName == "username") this.errorMessages[controlName] = this.USERNAME_VALIDATION_RULE;
    }
    else if (control.hasError('email')) {
      this.errorMessages[controlName] = this.EMAIL_VALIDATION_RULE;
    }
    else if (control.hasError('pattern')) {
      this.errorMessages[controlName] = this.PASSWORD_VALIDATION_RULE;
    } else {
      this.errorMessages[controlName] = '';
    }
  }

  /**
   * Lifecycle hook called when the component is about to be destroyed.
   * Unsubscribes from the `authServiceSubscription` if it exists.
   *
   * @return {void} - Does not return anything.
   */
  ngOnDestroy(): void {
    if (this.authServiceSubscription){
      this.authServiceSubscription.unsubscribe();
    }
  }

}
