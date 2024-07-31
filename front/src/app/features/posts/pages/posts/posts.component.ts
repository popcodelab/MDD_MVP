import {Component, OnDestroy, OnInit} from '@angular/core';
import {DatePipe, TitleCasePipe} from "@angular/common";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {Post} from "../../interfaces/post";
import {Subscription, switchMap} from "rxjs";
import {PostService} from "../../services/post.service";
import {SessionService} from "../../../auth/services/session.service";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {Topic} from "../../../topics/interfaces/topic";

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    DatePipe,
    RouterLink,
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatCardTitle,
    MatCardSubtitle,
    TitleCasePipe
  ],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss','../../../../shared/styles/topic-card-styles.css']
})
export class PostsComponent implements OnInit, OnDestroy {
  sortBy: string | null = null;
  sortDirection: string | null = null;
  posts: Post[] = [];
  sortDirections: { [key: string]: string } = {
    date: 'asc',
    title: 'asc',
  };

  private postServiceSubscription: Subscription | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private sessionService: SessionService
  ) {
  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    this.getQueryParams();
    this.getTopicUserPosts();
  }

  private getQueryParams(): void {
    this.route.queryParams.subscribe(params => {
      this.sortBy = params['sort'];
      this.sortDirection = params['direction'];
      if (!this.sortBy || !this.sortDirection) {
        this.swapSortOrder('date', 'desc');
      }
    });
  }

  private getTopicUserPosts(): void {
    this.postServiceSubscription = this.sessionService.topicSubscriptions$.pipe(
      switchMap((topics: Topic[]) => this.postService.getAllTopicPosts(topics.map((topic: Topic) => topic.id))),
      switchMap(() => this.postService.posts$)
    ).subscribe((posts: Post[]) => {
      this.posts = posts;
    });
  }

  onSortClick(sortBy: string, direction: string): void {
    this.sortDirections[sortBy] = this.toggleSortDirection(direction);
    this.swapSortOrder(sortBy, this.sortDirections[sortBy]);
  }

  private toggleSortDirection(currentDirection: string): string {
    return currentDirection === APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING ? APP_CONSTANTS.SORT_DIRECTIONS.DESCENDING
      : APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING;
  }

  private swapSortOrder(sortBy: string, direction: string): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {sort: sortBy, direction: direction},
      queryParamsHandling: 'merge'
    }).then(() => {
      this.postService.sortPostsBy(sortBy, direction);
    });
  }
}
