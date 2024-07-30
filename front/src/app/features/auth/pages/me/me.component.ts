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
import {Topic} from "../../../topics/interfaces/topic";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";

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
    MatDivider,
    MatCardActions,
    MatCardContent,
    MatCardTitle,
    MatCardHeader,
    MatCard
  ],
  templateUrl: './me.component.html',
  styleUrl: './me.component.scss'
})
export class MeComponent implements OnInit, OnDestroy {


  private userSubscription: Subscription | null = null;
  private topicsSubscription: Subscription | null = null;


  loggedUser: User | null = null;
  subscribedTopics: Topic[] = [];



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

  onUnsubscribeClick(topicId: number): void{
    this.userService.unsubscribeTopic(topicId).subscribe(
      (user: User) =>{
        this.sessionService.updateUser(user);
      }
    );
  }

  onBlur(controlName: string): void {
    const control: FormControl<any> = this.formControls[controlName];
    control.markAsTouched();
    this.errorMessages[controlName] = control.hasError('required') ? `Please enter ${this.controlNames[controlName]}` : '';
  }

  private isFormValid(): boolean {
    return this.formControls["username"].valid && this.formControls['email'].valid;
  }


  onSubmit() {
    if (this.isFormValid()) {
      if (this.loggedUser && this.loggedUser.id !== undefined) {
        if (this.loggedUser.id !== null) {
          const updatedUser: User = {
            id: this.loggedUser.id,
            username: this.formControls['username'].value,
            email: this.formControls['email'].value,
            password: this.loggedUser.password,
            subscribedTopicIds: this.loggedUser.subscribedTopicIds
          };
          this.userService.updateUser(updatedUser).subscribe((user: User) => {
            this.sessionService.updateUser(user);
          });
        } else {
          this.sessionService.logout();
        }
      } else {
        this.sessionService.logout();
      }
    }
  }



  onLogout(): void {
    this.sessionService.logout();
    this.router.navigate(['/login'])
  }


  /**
   * Lifecycle hook that is called when a component is destroyed.
   * This method unsubscribes from any active subscriptions to avoid memory leaks.
   */
  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
    if (this.topicsSubscription) {
      this.topicsSubscription.unsubscribe();
    }
  }

  /**
   * Initializes the component and sets up subscriptions for user and topics.
   */
  ngOnInit(): void {
    /**
     * Represents the user's subscription information.
     */
    this.userSubscription = this.sessionService.sessionUser$.subscribe((user: User | null) => {
      this.loggedUser = user;
      if (this.loggedUser) {
        console.log('Logged-in user : ', this.loggedUser);
        this.formControls['username'].setValue(this.loggedUser.username);
        this.formControls['email'].setValue(this.loggedUser.email);
      } else {
        this.formControls['username'].setValue('');
        this.formControls['email'].setValue('');
      }
    });
    /**
     * Represents a user's topics subscription.
     */
    this.topicsSubscription = this.sessionService.topicSubscriptions$.subscribe((topics: Topic[]) => {
      this.subscribedTopics = topics;
    });
  }

}
