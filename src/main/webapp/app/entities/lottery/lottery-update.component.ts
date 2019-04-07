import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ILottery } from 'app/shared/model/lottery.model';
import { LotteryService } from './lottery.service';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile';

@Component({
    selector: 'jhi-lottery-update',
    templateUrl: './lottery-update.component.html'
})
export class LotteryUpdateComponent implements OnInit {
    private _lottery: ILottery;
    isSaving: boolean;

    userprofiles: IUserProfile[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private lotteryService: LotteryService,
        private userProfileService: UserProfileService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ lottery }) => {
            this.lottery = lottery;
        });
        this.userProfileService.query().subscribe(
            (res: HttpResponse<IUserProfile[]>) => {
                this.userprofiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.lottery.id !== undefined) {
            this.subscribeToSaveResponse(this.lotteryService.update(this.lottery));
        } else {
            this.subscribeToSaveResponse(this.lotteryService.create(this.lottery));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILottery>>) {
        result.subscribe((res: HttpResponse<ILottery>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserProfileById(index: number, item: IUserProfile) {
        return item.id;
    }
    get lottery() {
        return this._lottery;
    }

    set lottery(lottery: ILottery) {
        this._lottery = lottery;
    }
}
