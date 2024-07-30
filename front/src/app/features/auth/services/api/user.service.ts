import {Injectable} from '@angular/core';
import {environment} from "../../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../../interfaces/User";

@Injectable({
  providedIn: 'root'
})

/**
 * UserService class that provides methods for interacting with user data.
 */
export class UserService {

  /**
   * Represents the URL for accessing the user's profile information.
   *
   * @type {string}
   * @memberof global
   */
  private apiUrl: string = environment.apiUrl + '/me';

  constructor(private http: HttpClient) {
  }

  /**
   * Retrieves the user information from the API.
   *
   * @return {Observable<User>} An Observable that emits the user information.
   */
  public getUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}`)
  }

  /**
   * Updates a user in the database.
   *
   * @param {User} user - The user to be updated.
   * @return {Observable<User>} - An Observable that emits the updated User after successfully updating it in the database.
   */
  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}`, user);
  }

  /**
   * User subscribes to a topic.
   *
   * @param {number} topicId - The ID of the topic to subscribe to.
   * @return {Observable<User>} - An observable that emits the subscribed user.
   */
  public subscribeTopic(topicId: number): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/topics/${topicId}`, {});
  }

  /**
   * User unsubscribes from a topic.
   *
   * @param {number} topicId - The id of the topic to unsubscribe from.
   * @return {Observable<User>} - An observable that emits a User object when the unsubscribe operation is successful.
   */
  public unsubscribeTopic(topicId: number): Observable<User> {
    return this.http.delete<User>(`${this.apiUrl}/topic/${topicId}`);
  }
}
