import {Component, OnDestroy} from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MaterialModule} from "../../../../shared/material.modules";
import {Subscription} from "rxjs";
import {Router, RouterLink} from "@angular/router";
import {RegisterRequest} from "../../../../core/interfaces/auth/register.request";
import {AuthenticationService} from "../../services/authentication.service";
import {APP_CONSTANTS} from "../../../../shared/constants";

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
    username: APP_CONSTANTS.VALIDATION_MESSAGES.USERNAME_VALIDATION_RULE,
    email: APP_CONSTANTS.VALIDATION_MESSAGES.EMAIL_VALIDATION_RULE,
    password: APP_CONSTANTS.VALIDATION_MESSAGES.PASSWORD_VALIDATION_RULE,
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

  /**
   * Constructs a new instance of the class.
   *
   * @param {AuthenticationService} authService - The authentication service responsible for authenticating and authorizing users.
   * @param {Router} router - The router service used for navigation in the application.
   */
  constructor(private authService: AuthenticationService,
              private router: Router) {
  }

  /**
   * Handles form submission
   *
   * This method is called when a form submission event occurs. It checks if the username, email,
   * and password fields are valid. If all three fields are valid, it creates a RegisterRequest
   * object using the values from the form fields and calls the "handleRegistration" method.
   *
   * @returns {void} No return value
   */
  onSubmit(): void {
    if (this.formControls["username"].valid && this.formControls['email'].valid && this.formControls['password'].valid) {
      const registerRequest: RegisterRequest = this.createRegisterRequest();
      this.handleRegistration(registerRequest);
    }
  }

  /**
   * Creates a register request object with the provided form input values.
   *
   * @private
   * @return {RegisterRequest} - Register request object containing username, email, and password.
   */
  private createRegisterRequest(): RegisterRequest {
    return {
      username: this.formControls['username'].value,
      email: this.formControls['email'].value,
      password: this.formControls['password'].value,
    };
  }

  /**
   * Handles the registration request.
   *
   * @param {RegisterRequest} registerRequest - The registration request object containing user details.
   *
   * @private
   *
   * @return {void}
   */
  private handleRegistration(registerRequest: RegisterRequest): void {
    this.authServiceSubscription = this.authService.register(registerRequest).subscribe({
      next: () => {
        this.router.navigate(['/posts']).then(() => {
        });
      },
      error: error => {
        throw error;
      }
    });
  }

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
      this.errorMessages[controlName] = APP_CONSTANTS.ERROR_MESSAGES.PLEASE_ENTER + `${this.controlNames[controlName]}`;
    } else if (control.hasError('minlength')) {
      if (controlName == "username") this.errorMessages[controlName] = APP_CONSTANTS.VALIDATION_MESSAGES.USERNAME_VALIDATION_RULE;
    } else if (control.hasError('email')) {
      this.errorMessages[controlName] = APP_CONSTANTS.VALIDATION_MESSAGES.EMAIL_VALIDATION_RULE;
    } else if (control.hasError('pattern')) {
      this.errorMessages[controlName] = APP_CONSTANTS.VALIDATION_MESSAGES.PASSWORD_VALIDATION_RULE;
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
    if (this.authServiceSubscription) {
      this.authServiceSubscription.unsubscribe();
    }
  }

}
