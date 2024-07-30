import {Injectable} from '@angular/core';
import {BehaviorSubject, catchError, firstValueFrom, Observable} from "rxjs";
import {Router} from "@angular/router";
import {User} from "../interfaces/User";
import {Topic} from "../../topics/interfaces/topic";
import {UserService} from "./api/user.service";
import {TopicService} from "../../topics/services/topic.service";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private _sessionUser: BehaviorSubject<User | null > = new BehaviorSubject<User | null>(null);
  public sessionUser$: Observable<User | null> = this._sessionUser.asObservable();

  private _topicSubscriptions:BehaviorSubject<Topic[]> = new BehaviorSubject<Topic[]>([]);
  public topicSubscriptions$: Observable<Topic[]> = this._topicSubscriptions.asObservable();

  private _isUserLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public isUserLoggedIn$: Observable<any> = this._isUserLoggedIn.asObservable();

  constructor(private userService: UserService,
              private topicService: TopicService) {
    this.setUpUserSession().then(r => {});
  }

  public login(token: string): void {
    localStorage.setItem('token', token);
    this._isUserLoggedIn.next(true);
    this.setUpUserSession();
  }

  private async setUpUserSession(): Promise<void> {
    const token: string | null= localStorage.getItem('token');
    if (token) {
      try {
        const user: void | User = await firstValueFrom(
          this.userService.getUser().pipe(catchError(error => this.handleUnauthorizedAccess(error)))
        );
        if (user) {
          this._sessionUser.next(user);
          const topics: Topic[] = await firstValueFrom(this.topicService.getAllTopics());
          if (topics) {
            const topicSubscriptions: Topic[] = topics.filter((topic: Topic) => user.subscribedTopicIds.includes(topic.id));
            this._topicSubscriptions.next(topicSubscriptions);
          }
          this._isUserLoggedIn.next(true);
        }
      } catch (error) {
        throw error;
      }
    }
  }

  public logout(): void {
    localStorage.removeItem('token');
    this.resetUserSession();
  }

  private resetUserSession(): void {
    this._sessionUser.next(null);
    this._isUserLoggedIn.next(false);
    this._topicSubscriptions.next([]);
  }

  private handleUnauthorizedAccess(error: any): Promise<void> {
    if (error.status === 401) {
      localStorage.removeItem('token');
      this._isUserLoggedIn.next(false);
    }
    throw error;
  }

  public updateUser(modifiedUser: User): void {
    this._sessionUser.next(modifiedUser);
    this.updateUserTopicSubscriptions(modifiedUser);
  }

  private updateUserTopicSubscriptions(modifiedUser: User): void {
    this.topicService.getAllTopics().subscribe((topic: Topic[]) => {
      const topicSubscriptions: Topic[] = topic.filter((topic: Topic) => modifiedUser.subscribedTopicIds.includes(topic.id));
      this._topicSubscriptions.next(topicSubscriptions);
    });
  }

}
