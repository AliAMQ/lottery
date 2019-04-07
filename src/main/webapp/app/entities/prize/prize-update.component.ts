import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPrize } from 'app/shared/model/prize.model';
import { PrizeService } from './prize.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { ILottery } from 'app/shared/model/lottery.model';
import { LotteryService } from 'app/entities/lottery';
import { IHistory } from 'app/shared/model/history.model';
import { HistoryService } from 'app/entities/history';

@Component({
    selector: 'jhi-prize-update',
    templateUrl: './prize-update.component.html'
})
export class PrizeUpdateComponent implements OnInit {
    private _prize: IPrize;
    isSaving: boolean;

    categories: ICategory[];

    lotteries: ILottery[];

    histories: IHistory[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private prizeService: PrizeService,
        private categoryService: CategoryService,
        private lotteryService: LotteryService,
        private historyService: HistoryService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ prize }) => {
            this.prize = prize;
        });
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.lotteryService.query().subscribe(
            (res: HttpResponse<ILottery[]>) => {
                this.lotteries = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.historyService.query().subscribe(
            (res: HttpResponse<IHistory[]>) => {
                this.histories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.prize.id !== undefined) {
            this.subscribeToSaveResponse(this.prizeService.update(this.prize));
        } else {
            this.subscribeToSaveResponse(this.prizeService.create(this.prize));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrize>>) {
        result.subscribe((res: HttpResponse<IPrize>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackLotteryById(index: number, item: ILottery) {
        return item.id;
    }

    trackHistoryById(index: number, item: IHistory) {
        return item.id;
    }
    get prize() {
        return this._prize;
    }

    set prize(prize: IPrize) {
        this._prize = prize;
    }
}
