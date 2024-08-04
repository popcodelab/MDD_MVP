import {Component, OnDestroy, OnInit} from '@angular/core';
import {Post} from "../../interfaces/post";
import {Comment} from "../../interfaces/comment";
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Params, RouterLink} from "@angular/router";
import {PostService} from "../../services/post.service";
import {Subscription} from "rxjs";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {MatIcon} from "@angular/material/icon";
import {DatePipe, NgClass, NgForOf, NgIf, TitleCasePipe} from "@angular/common";
import {MatDivider} from "@angular/material/divider";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {PostCommentComponent} from "../../../../core/components/post-comment/post-comment.component";

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [
    RouterLink,
    MatIcon,
    MatLabel,
    MatError,
    DatePipe,
    NgIf,
    MatDivider,
    FormsModule,
    NgClass,
    MatFormField,
    ReactiveFormsModule,
    MatInput,
    NgForOf,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardSubtitle,
    MatCardTitle,
    TitleCasePipe,
    PostCommentComponent
  ],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.scss'
})
export class PostDetailsComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  post: Post | undefined;
  comments: Comment[] = [];
  formControls: { [key: string]: FormControl } = {
    content: new FormControl('', [Validators.required])
  };

  labels: { [key: string]: string } = {
    content: 'Add a comment...'
  };

  controlNames: { [key: string]: string } = {
    content: 'content'
  };

  errorMessages: { [key: string]: string } = {
    content: ''
  };

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
  ) {
  }

  private initializeGetPostCommentsSubscription(): void {
    let getPostCommentsSubscription: Subscription = this.route.params.subscribe(params => {
      this.initializePostServiceSubscription(params);
    });
    this.subscriptions.push(getPostCommentsSubscription);
  }

  private initializePostServiceSubscription(params: Params): void {
    const id: number = +params['id'];
    let postServiceSubscription: Subscription = this.postService.getPostById(id).subscribe((post: Post) => {
      this.post = post;
      this.initializeGetCommentsSubscription(id);
    });
    this.subscriptions.push(postServiceSubscription);
  }

  private initializeGetCommentsSubscription(id: number): void {
    this.postService.getComments(id).subscribe((comments: Comment[]) => {
      this.comments = comments;
    });
  }

  private getErrorMessageForControl(control: FormControl, controlName: string): string {
    // Extract method: encapsulates the logic of generating error message
    return control.hasError('required') ? `${APP_CONSTANTS.ERROR_MESSAGES.PLEASE_ENTER} ${this.controlNames[controlName]}` : '';
  }


  ngOnInit(): void {
    this.initializeGetPostCommentsSubscription();
  }

  onBlur(controlName: string): void {
    const control: FormControl<any> = this.formControls[controlName];
    control.markAsTouched();
    this.errorMessages[controlName] = this.getErrorMessageForControl(control, controlName);
  }

  onAddCommentClick() {
    if (this.formControls['content'].valid && this.post?.id) {
      const newComment: Pick<Comment, 'postId' | 'content'> = {
        content: this.formControls['content'].value,
        postId: this.post.id,
      };

      let addCommentSubscription: Subscription = this.postService.addComment(newComment).subscribe({
        next: (newComment: Comment) => {
          this.comments.push(newComment);
          this.formControls['content'].reset();
          this.formControls['content'].setErrors(null);
        },
        error: error => {
          throw error;
        }
      });
      this.subscriptions.push(addCommentSubscription);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription: Subscription) => subscription.unsubscribe());
  }

}
