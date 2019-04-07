import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Prize } from 'app/shared/model/prize.model';
import { PrizeService } from './prize.service';
import { PrizeComponent } from './prize.component';
import { PrizeDetailComponent } from './prize-detail.component';
import { PrizeUpdateComponent } from './prize-update.component';
import { PrizeDeletePopupComponent } from './prize-delete-dialog.component';
import { IPrize } from 'app/shared/model/prize.model';

@Injectable({ providedIn: 'root' })
export class PrizeResolve implements Resolve<IPrize> {
    constructor(private service: PrizeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((prize: HttpResponse<Prize>) => prize.body));
        }
        return of(new Prize());
    }
}

export const prizeRoute: Routes = [
    {
        path: 'prize',
        component: PrizeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'lotteryApp.prize.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prize/:id/view',
        component: PrizeDetailComponent,
        resolve: {
            prize: PrizeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.prize.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prize/new',
        component: PrizeUpdateComponent,
        resolve: {
            prize: PrizeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.prize.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prize/:id/edit',
        component: PrizeUpdateComponent,
        resolve: {
            prize: PrizeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.prize.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prizePopupRoute: Routes = [
    {
        path: 'prize/:id/delete',
        component: PrizeDeletePopupComponent,
        resolve: {
            prize: PrizeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lotteryApp.prize.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
