import {Injectable} from '@angular/core';
import {BehaviorSubject, mergeMap, Observable, tap} from "rxjs";
import {environment} from "../../../../environments/environment";
import {Post} from "../interfaces/post";
import {HttpClient} from "@angular/common/http";
import {SessionService} from "../../auth/services/session.service";
import {Topic} from "../../topics/interfaces/topic";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl: string = environment.apiUrl + '/posts';
  private _posts: BehaviorSubject<Post[]> = new BehaviorSubject<Post[]>([]);
  public posts$: Observable<Post[]> = this._posts.asObservable();

  constructor(private httpClient: HttpClient,
              private sessionService: SessionService) {
    this.sessionService.topicSubscriptions$.pipe(
      mergeMap((topics: Topic[]) => {
        const topicsIds: number[] = topics.map((topic: Topic) => topic.id);
        return this.getAllTopicPosts(topicsIds);
      })

    ).subscribe();
  }

  public getAllTopicPosts(topicsIds: number[]): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.apiUrl).pipe(
      tap((posts: Post[]) => {
        this.filterPostsByTopicIds(posts, topicsIds);
      })
    );
  }

  private filterPostsByTopicIds(posts: Post[], topicsIds: number[]): void {
    const filteredPosts: Post[] = posts.filter((post: Post) => topicsIds.includes(post.topicId));
    this._posts.next(filteredPosts);
  }

  private sortByDate(a: Post, b: Post): number {
    return new Date(b.created_at).getTime() - new Date(a.created_at).getTime();
  }

  private sortByTitle(a: Post, b: Post): number {
    return b.title.localeCompare(a.title);
  }

  sortPostsBy(sortBy: string, direction: string = 'desc'): void {
    if (!this._posts.value) return;
    switch (sortBy) {
      case 'date':
        this._posts.value.sort(this.sortByDate);
        break;
      case 'title':
        this._posts.value.sort(this.sortByTitle);
        break;
    }
    if (direction === 'asc') {
      this._posts.value.reverse();
    }
  }

}
