import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {MatButton} from "@angular/material/button";
import {NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {User} from "../../interfaces/User";
import {Router} from "@angular/router";
import {SessionService} from "../../services/session.service";
import {UserService} from "../../services/api/user.service";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-me',
  standalone: true,
  imports: [
    MatError,
    MatLabel,
    MatFormField,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    MatButton,
    NgIf,
    MatDivider
  ],
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss'
})
export class MeComponent implements OnInit, OnDestroy {

  loggedUser: User | null = null;
  private userSubscription: Subscription | null = null;

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

  constructor(private sessionService: SessionService,
              private userService: UserService,
              private router: Router) {}

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  private isFormValid(): boolean {
    return this.formControls["username"].valid && this.formControls['email'].valid;
  }


  onSubmit() {
    if (this.isFormValid()) {
      if (this.loggedUser && this.loggedUser.id !== undefined && this.loggedUser.id !== null) {
        const updatedUser: User = {
          id: this.loggedUser.id,
          username: this.formControls['username'].value,
          email: this.formControls['email'].value,
          password: this.loggedUser.password,
          subscribedTopicIds: this.loggedUser.subscribedTopicIds
        };
        this.userService.updateUser(updatedUser).subscribe((user) => {
          this.sessionService.updateUser(user);
        });
      } else {
        this.sessionService.logout();
      }
    }
  }

  onBlur(controlName: string): void {
    const control: FormControl<any> = this.formControls[controlName];
    control.markAsTouched();
    this.errorMessages[controlName] = control.hasError('required') ? `Please enter ${this.controlNames[controlName]}` : '';
  }

  onLogout(): void {
    this.sessionService.logout();
    this.router.navigate(['/login'])
  }

}
