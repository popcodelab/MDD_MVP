import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule, DatePipe, NgClass, NgForOf, TitleCasePipe} from "@angular/common";
import {ActivatedRoute, Router, RouterLink, RouterModule, RouterOutlet} from "@angular/router";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {Post} from "../../interfaces/post";
import {Subscription, switchMap} from "rxjs";
import {PostService} from "../../services/post.service";
import {SessionService} from "../../../auth/services/session.service";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {Topic} from "../../../topics/interfaces/topic";
import {MatDivider} from "@angular/material/divider";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatAnchor, MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

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
    TitleCasePipe,
    MatDivider,
    MatMenu,
    MatMenuTrigger,
    MatAnchor,
    MatMenuItem,
    MatIcon,
    NgClass,
    MatButton,
    NgForOf
  ],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss','../../../../shared/styles/topic-card-styles.css']
})
export class PostsComponent implements OnInit, OnDestroy {
  /**
   * Represents the subscription for a PostService.
   */
  private postServiceSubscription: Subscription | null = null;

  sortBy: string | null = "date";
  sortDirection: string | null = null;
  sortDirections: { [key: string]: string } = {
    date: APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING,
    title: APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING,
  };
  ascendingOrder : boolean = true;
  posts: Post[] = [];



  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private sessionService: SessionService
  ) {  }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    this.getQueryParams();
    this.getTopicUserPosts();
  }

  trackByPostId(index: number, post: any): number {
    return post.id;
  }

  private getTopicUserPosts(): void {
    this.postServiceSubscription = this.sessionService.topicSubscriptions$.pipe(
      switchMap((topics: Topic[]) => this.postService.getAllTopicPosts(topics.map((topic: Topic) => topic.id))),
      switchMap(() => this.postService.posts$)
    ).subscribe((posts: Post[]) => {
      this.posts = posts;
    });
  }


  private getQueryParams(): void {
    this.route.queryParams.subscribe(params => {
      if(params) {
        if (params['sort']){
          this.sortBy = params['sort'];
        }
        this.sortDirection = params['direction'];
      if (!this.sortBy || !this.sortDirection) {
          this.swapSortOrder('date', 'desc');
        }
      }
    });
  }


  onSortClick(sortBy: string, direction: string): void {
    console.debug("onSortClick - sortBy : " + sortBy + " - direction : " + direction);
    console.table(this.sortDirections);
    this.sortDirections[sortBy] = this.toggleSortDirection(direction);
    this.swapSortOrder(sortBy, this.sortDirections[sortBy]);

    this.ascendingOrder = this.sortDirections[sortBy]===APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING;
  }

  private toggleSortDirection(currentDirection: string): string {
    return currentDirection === APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING ? APP_CONSTANTS.SORT_DIRECTIONS.DESCENDING
      : APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING;
  }

  private swapSortOrder(sortBy: string, direction: string): void {
    console.debug("swapSortOrder : sortBy : " + sortBy + " - direction : " + direction);
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {sort: sortBy, direction: direction},
      queryParamsHandling: 'merge'
    }).then(() => {
      this.postService.sortPostsBy(sortBy, direction);
      this.sortBy = sortBy;
    });
  }

  public newPost(){
    this.router.navigate(['/new']);
  }

}
