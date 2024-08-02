import {Injectable} from '@angular/core';
import {BehaviorSubject, mergeMap, Observable, of, switchMap, take, tap} from "rxjs";
import {environment} from "../../../../environments/environment";
import {Post} from "../interfaces/post";
import {Comment} from "../interfaces/comment";
import {HttpClient} from "@angular/common/http";
import {SessionService} from "../../auth/services/session.service";
import {Topic} from "../../topics/interfaces/topic";
import {APP_CONSTANTS} from "../../../shared/constants";
import {User} from "../../auth/interfaces/User";

/**
 * Represents a service that provides methods to interact with posts.
 */
@Injectable({
  providedIn: 'root'
})
export class PostService {

  /**
   * The URL for the API endpoint to get posts.
   *
   * @type {string}
   */
  private apiUrl: string = environment.apiUrl + '/posts';
  /**
   * Represents a BehaviorSubject variable for posts.
   */
  private _posts: BehaviorSubject<Post[]> = new BehaviorSubject<Post[]>([]);
  /**
   * Represents the observable variable 'posts$', which emits an array of Post objects.

   * @description
   * This variable is used to obtain an observable that emits an array of Post objects. It is an
   * instance of the Observable class and provides a convenient way to retrieve the current value
   * as an array of Post objects, or subscribe to future updates.
   */
  public posts$: Observable<Post[]> = this._posts.asObservable();

  /**
   * Constructor for initializing an instance of the class.
   *
   * @param {HttpClient} httpClient - The HttpClient instance to be used for making HTTP requests.
   * @param {SessionService} sessionService - The SessionService instance for managing user sessions.
   */
  constructor(private httpClient: HttpClient,
              private sessionService: SessionService) {
    this.sessionService.topicSubscriptions$.pipe(
      mergeMap((topics: Topic[]) => {
        const topicsIds: number[] = topics.map((topic: Topic) => topic.id);
        return this.getAllTopicPosts(topicsIds);
      })
    ).subscribe();
  }

  /**
   * Retrieve all posts related to the specified topic IDs.
   *
   * @param {number[]} topicsIds - An array of topic IDs to retrieve posts for.
   * @return {Observable<Post[]>} - An Observable that emits an array of Post objects.
   */
  public getAllTopicPosts(topicsIds: number[]): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.apiUrl).pipe(
      tap((posts: Post[]) => {
        this.filterPostsByTopicIds(posts, topicsIds);
      })
    );
  }

  /**
   * Retrieves a post by its ID.
   *
   * @param {number} id - The ID of the post to retrieve.
   * @returns {Observable<Post>} - An Observable that emits the retrieved Post object.
   */
  getPostById(id: number): Observable<Post> {
    return of(this._posts.value.find((post: Post) => post.id === id)).pipe(
      switchMap((post: Post | undefined) => {
          return post
            ? of(post)
            : this.httpClient.get<Post>(`${this.apiUrl}/${id}`).pipe(
              tap((fetchedPost: Post) => {
                this._posts.next([...this._posts.value, fetchedPost]);
              })
            );
        }
      )
    );
  }

  /**
   * Filters an array of posts by comparing their topic IDs with the given topic IDs.
   *
   * @param {Post[]} posts - The array of posts to be filtered.
   * @param {number[]} topicsIds - The array of topic IDs to filter the posts by.
   *
   * @private
   */
  private filterPostsByTopicIds(posts: Post[], topicsIds: number[]): void {
    const filteredPosts: Post[] = posts.filter((post: Post) => topicsIds.includes(post.topicId));
    this._posts.next(filteredPosts);
  }

  /**
   * Sorts an array of Posts by date.
   *
   * @param {Post} a - The first Post object to compare.
   * @param {Post} b - The second Post object to compare.
   * @return {number} - A negative number if `a` is earlier than `b`,
   *                   zero if they are the same date,
   *                   or a positive number if `a` is later than `b`.
   * @private
   */
  private sortByDate(a: Post, b: Post): number {
    return new Date(b.created_at).getTime() - new Date(a.created_at).getTime();
  }

  /**
   * Sorts an array of Post objects by their title in descending order.
   *
   * @param {Post} a - The first Post object to compare.
   * @param {Post} b - The second Post object to compare.
   * @returns {number} - A negative number if `a` is alphabetically before `b` based on their title,
   *                     a positive number if `a` is alphabetically after `b` based on their title,
   *                     or zero if their titles are identical or cannot be compared.
   * @private
   */
  private sortByTitle(a: Post, b: Post): number {
    return b.title.localeCompare(a.title);
  }

  /**
   * Sorts the posts array by the given sortField and sortOrder.
   *
   * @param {string} sortField - The field to sort by.
   * @param {string} [sortOrder='desc'] - The sort direction.
   * @return {void}
   */
  sortPostsBy(sortField: string, sortOrder: string = 'desc'): void {
    console.debug(`Sort by: ${sortField} - Direction: ${sortOrder}`);

    if (!this._posts.value) return;

    let sortFunction = this.getSortFunctionBy(sortField);
    this._posts.value.sort(sortFunction);
    this.applySortDirection(sortOrder);
  }

  /**
   * Returns a sorting function based on the provided sort field.
   *
   * @param {string} sortField - The field to sort by. Possible values are 'date' and 'title'.
   * @private
   * @return {(a: Post, b: Post) => number} - A sorting function that takes two Post objects as arguments and returns a number representing the order.
   */
  private getSortFunctionBy(sortField: string): (a: Post, b: Post) => number {
    switch (sortField) {
      case 'date':
        return this.sortByDate;
      case 'title':
        return this.sortByTitle;
      default:
        return this.sortByDate;
    }
  }

  /**
   * Applies the specified sort direction to the posts array.
   *
   * @param {string} sortOrder - The sort direction to apply. Can be either 'asc' or 'desc'.
   * @private
   * @return {void}
   */
  private applySortDirection(sortOrder: string): void {
    if (sortOrder === 'asc') {
      this._posts.value.reverse();
    }
  }

  /**
   * Creates a new post.
   *
   * @param post - The post object containing the title, content, and topicId.
   * @returns An Observable that emits the created post.
   * @throws Error if the user is not authenticated.
   */
  createPost(post: Pick<Post, 'title' | 'content' | 'topicId'>): Observable<Post> {
    return this.sessionService.sessionUser$.pipe(
      take(1),
      switchMap(loggedUser => {
        if (!loggedUser) {
          throw new Error(APP_CONSTANTS.ERROR_MESSAGES.MUST_BE_AUTHENTICATED);
        }
        return this.httpClient.post<Post>(this.apiUrl, this.createPostFromInput(post, loggedUser));
      })
    );
  }

  /**
   * Creates a new post object based on the inputPost and user data.
   *
   * @param {Pick<Post, 'title' | 'content' | 'topicId'>} inputPost - The input post object containing title, content, and topic ID.
   * @param {User} user - The user object representing the creator of the post.
   * @returns {Omit<Post, 'id' | 'topicTitle' | 'commentIds'>} The newly created post object without ID, topic title, and comment IDs.
   * @private
   */
  private createPostFromInput(inputPost: Pick<Post, 'title' | 'content' | 'topicId'>, user: User): Omit<Post, 'id' | 'topicTitle' | 'commentIds'> {
    return {
      title: inputPost.title,
      content: inputPost.content,
      username: user.username,
      userId: user.id,
      topicId: inputPost.topicId,
      created_at: new Date()
    };
  }

  getComments(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.apiUrl}/${postId}/comments`);
  }

  addComment(comment: Pick<Comment, 'postId' | 'content'>): Observable<Comment> {
    return this.sessionService.sessionUser$.pipe(
      take(1),
      switchMap(currentUser => {
        if (!currentUser) {
          throw new Error(APP_CONSTANTS.ERROR_MESSAGES.MUST_BE_AUTHENTICATED);
        }
        const commentDto: Omit<Comment, 'id'> = {
          content: comment.content,
          userId: currentUser.id,
          username: currentUser.username,
          postId: comment.postId,
          created_at: new Date(),
        };
        console.debug(currentUser.username + " is adding a new comment to the post :  " + comment.postId + " : " + commentDto.content)
        return this.httpClient.post<Comment>(`${this.apiUrl}/${comment.postId}/comments`, commentDto);
      })
    );
  }

}
