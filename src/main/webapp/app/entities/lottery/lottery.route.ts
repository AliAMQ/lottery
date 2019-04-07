import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Lottery } from 'app/shared/model/lottery.model';
import { LotteryService } from './lottery.service';
import { LotteryComponent } from './lottery.component';
import { LotteryDetailComponent } from './lottery-detail.component';
import { LotteryUpdateComponent } from './lottery-update.component';
import { LotteryDeletePopupComponent } from './lottery-delete-dialog.component';
import { ILottery } from 'app/shared/model/lottery.model';

@Injectable({ providedIn: 'root' })
export class LotteryResolve implements Resolve<ILottery> {
    constructor(private service: LotteryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((lottery: HttpResponse<Lottery>) => lottery.body));
        }
        return of(new Lottery());
    }
}

export const lotteryRoute: Routes = [
    {
        path: 'lottery',
        component: LotteryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'lotteryApp.lottery.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lottery/:id/view',
        component: LotteryDetailComponent,
        resolve: {
            lottery: LotteryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.lottery.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lottery/new',
        component: LotteryUpdateComponent,
        resolve: {
            lottery: LotteryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.lottery.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lottery/:id/edit',
        component: LotteryUpdateComponent,
        resolve: {
            lottery: LotteryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.lottery.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lotteryPopupRoute: Routes = [
    {
        path: 'lottery/:id/delete',
        component: LotteryDeletePopupComponent,
        resolve: {
            lottery: LotteryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.lottery.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
