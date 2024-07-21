import {Component, OnDestroy} from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Subject, Subscription, takeUntil} from "rxjs";
import {Router, RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel, MatError} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {LoginRequest} from "../../../../core/interfaces/auth/login.request";
import {AuthenticationService} from "../../services/authentication.service";
import {SessionService} from "../../services/session.service";
import {LoginResponse} from "../../../../core/interfaces/auth/login.response";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    NgClass,
    RouterLink,
    ReactiveFormsModule,
    FormsModule,
    NgIf,
    MatIcon,
    MatLabel,
    MatFormField,
    MatError,
    MatInput,
    MatButton
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnDestroy {

  private readonly EMAIL_OR_USERNAME: string = "Email or username";
  private readonly PASSWORD: string = "Password";

  // AuthenticationService subscription
  private authServiceSubscription: Subscription | undefined;

  // Object that contains form control objects.
  formControls: { [key: string]: FormControl } = {
    emailOrUsername: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  };

  labels: { [key: string]: string } = {
    emailOrUsername: this.EMAIL_OR_USERNAME,
    password: this.PASSWORD,
  };

  controlNames: { [key: string]: string } = {
    emailOrUsername: this.EMAIL_OR_USERNAME,
    password: this.PASSWORD,
  };

  formValidationMessages: { [key: string]: string } = {
    emailOrUsername: '',
    password: '',
  };

  loginErrorMessage: string | null = null;

  constructor(private authenticationService: AuthenticationService,
              private sessionService: SessionService,
              private router: Router) {
  }

  onBlur(controlName: string): void {
    const control: FormControl<any> = this.formControls[controlName];
    control.markAsTouched();
    this.formValidationMessages[controlName] = control.hasError('required') ?
      `Please enter ${this.controlNames[controlName].toLowerCase()} ` : '';
  }

  onSubmit(): void {
    if (this.formControls['emailOrUsername'].valid && this.formControls['password'].valid) {
      this.login();
    }
  }


  login(): void {
    let emailOrUsername: string = this.formControls['emailOrUsername'].value;
    let password: string = this.formControls['password'].value;

    console.info(`login : ${emailOrUsername} - ${password}`);

    const loginRequest: LoginRequest = {
      emailOrUsername: emailOrUsername,
      password: password,
    };
    this.authServiceSubscription = this.authenticationService.login(loginRequest)
      .subscribe({
        next: (data: LoginResponse) => {
          this.sessionService.login(data.token);
          this.router.navigate(['/posts']).then(
            () => {
            }
          );
        },
        error: error => {
          throw error;
        }
      });
  }


  /**
   * Performs cleanup tasks before the component is destroyed.
   * Unsubscribe to the subscribed services
   * @returns {void}
   */
  ngOnDestroy(): void {
    if (this.authServiceSubscription) {
      this.authServiceSubscription.unsubscribe();
    }
  }
}
