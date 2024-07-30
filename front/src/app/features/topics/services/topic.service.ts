import {Injectable} from '@angular/core';
import {environment} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Topic} from "../interfaces/topic";
import {Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
/**
 * TopicService class
 *
 * This class provides methods for retrieving topics from an API.
 */
export class TopicService {

  /**
   * The URL used to make API requests for topics.
   *
   * @type {string}
   */
  private apiUrl: string = environment.apiUrl + '/topics';
  /**
   * Represents a list of topics.
   */
  private topics: Topic[] | null = null;

  /**
   * Constructs a new instance of the class.
   *
   * @param {HttpClient} httpClient - The HttpClient instance to be used for making HTTP requests.
   */
  constructor(private httpClient: HttpClient) {
  }

  /**
   * Retrieves all topics from the specified API URL using an HTTP GET request.
   *
   * @private
   * @returns {Observable<Topic[]>} An observable that emits an array of topics retrieved from the API.
   */
  private httpGetTopics(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(this.apiUrl).pipe(
      tap((topics: Topic[]) => this.topics = topics)
    );
  }

  /**
   * Retrieves all topics.
   *
   * @returns {Observable<Topic[]>} An Observable that emits the array of topics.
   *    If topics are already available, it returns an Observable of the existing topics.
   *    Otherwise, it makes an HTTP GET request to fetch the topics and returns the result as Observable.
   */
  getAllTopics(): Observable<Topic[]> {
    return this.topics ? of(this.topics) : this.httpGetTopics();
  }
}
