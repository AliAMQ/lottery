import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { History } from 'app/shared/model/history.model';
import { HistoryService } from './history.service';
import { HistoryComponent } from './history.component';
import { HistoryDetailComponent } from './history-detail.component';
import { HistoryUpdateComponent } from './history-update.component';
import { HistoryDeletePopupComponent } from './history-delete-dialog.component';
import { IHistory } from 'app/shared/model/history.model';

@Injectable({ providedIn: 'root' })
export class HistoryResolve implements Resolve<IHistory> {
    constructor(private service: HistoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((history: HttpResponse<History>) => history.body));
        }
        return of(new History());
    }
}

export const historyRoute: Routes = [
    {
        path: 'history',
        component: HistoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'lotteryApp.history.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'history/:id/view',
        component: HistoryDetailComponent,
        resolve: {
            history: HistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.history.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'history/new',
        component: HistoryUpdateComponent,
        resolve: {
            history: HistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.history.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'history/:id/edit',
        component: HistoryUpdateComponent,
        resolve: {
            history: HistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.history.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const historyPopupRoute: Routes = [
    {
        path: 'history/:id/delete',
        component: HistoryDeletePopupComponent,
        resolve: {
            history: HistoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.history.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
