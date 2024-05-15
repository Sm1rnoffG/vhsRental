package com.example.vhsrental.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vhsrental.data.exceptions.MovieExceptions
import com.example.vhsrental.data.models.DEFAULT_USER
import com.example.vhsrental.data.models.Role
import com.example.vhsrental.ui.screens.login.AccountUpdateScreen
import com.example.vhsrental.ui.screens.login.LoggedUserScreen
import com.example.vhsrental.ui.screens.login.LoginScreen
import com.example.vhsrental.ui.screens.login.PasswordUpdateScreen
import com.example.vhsrental.ui.screens.login.RegisterScreen
import com.example.vhsrental.ui.screens.movies.CatalogueScreen
import com.example.vhsrental.ui.screens.movies.MovieDetail
import com.example.vhsrental.ui.screens.movies.MovieEditScreen
import com.example.vhsrental.ui.screens.myorders.MyOrderDetailScreen
import com.example.vhsrental.ui.screens.myorders.MyOrdersScreen
import com.example.vhsrental.ui.screens.orders.CreateOrderScreen
import com.example.vhsrental.ui.screens.orders.OrderDetailScreen
import com.example.vhsrental.ui.screens.orders.OrderScreen
import com.example.vhsrental.ui.screens.users.UserDetailScreen
import com.example.vhsrental.ui.screens.users.UsersScreen
import com.example.vhsrental.ui.viewmodels.CreateOrderActions
import com.example.vhsrental.ui.viewmodels.LoginUiState
import com.example.vhsrental.ui.viewmodels.LoginViewModel
import com.example.vhsrental.ui.viewmodels.MovieActions
import com.example.vhsrental.ui.viewmodels.MovieEditActions
import com.example.vhsrental.ui.viewmodels.MovieEditingViewModel
import com.example.vhsrental.ui.viewmodels.MoviesViewModel
import com.example.vhsrental.ui.viewmodels.OrderActions
import com.example.vhsrental.ui.viewmodels.OrderCreatingViewModel
import com.example.vhsrental.ui.viewmodels.OrderViewModel
import com.example.vhsrental.ui.viewmodels.UpdateAccountActions
import com.example.vhsrental.ui.viewmodels.UserActions
import com.example.vhsrental.ui.viewmodels.UsersViewModel
import kotlin.math.log

@Composable
fun Navigation(
    movieVm: MoviesViewModel, editMovieVm: MovieEditingViewModel,
    loginVm: LoginViewModel,
    usersVm: UsersViewModel,
    orderVm: OrderViewModel, orderCreatingVm: OrderCreatingViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Tab.Login.route,
        modifier = modifier,
    ) {
        // LOGIN NAVIGATION
        composable(Tab.Login.route) {
            LoginScreen(
                toCatalogue = {
                    navController.navigate(Tab.Movies.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                toRegister = { navController.navigate(Screens.Register.name) },
                vm = loginVm
            )
        }
        composable(Screens.Register.name) {
            RegisterScreen(
                toLogin = { navController.popBackStack() },
                vm = loginVm,
            )
        }

        // MOVIES NAVIGATION
        composable(Tab.Movies.route) {
            CatalogueScreen(state = (movieVm.uiStateFlow.collectAsState().value),
                toMovieDetail = { movie ->
                    movieVm.emitAction(MovieActions.OpenMovieDetail(movie))
                    navController.navigate(Screens.MovieDetail.name)
                },
                onQuerryRequest = { /*TODO*/ })
        }
        composable(Screens.AddMovie.name) {
            MovieEditScreen(
                state = editMovieVm.uiStateFlow.collectAsState().value,
                onConfirm = {
                    editMovieVm.emitEditAction(MovieEditActions.OnAddMovie)
                    if (editMovieVm.uiStateFlow.value.errorMessage.isEmpty()) {
                        movieVm.emitAction(MovieActions.LoadCatalogue)
                        navController.popBackStack()
                    }
                },
                onValueChange = { action -> editMovieVm.emitEditAction(action) }
            )
        }
        composable(Screens.MovieDetail.name) {
            val loginState = loginVm.uiStateFlow.collectAsState().value
            if (loginState is LoginUiState.LoggedIn) {
                MovieDetail(
                    movie = movieVm.uiStateFlow.collectAsState().value.displayMovie
                        ?: throw MovieExceptions.UnexpectedException(),
                    onEdit = { movie ->
                        editMovieVm.emitEditAction(
                            MovieEditActions.OnLoadMovie(
                                movie
                            )
                        )
                    },
                    onReservation = { movie ->
                        orderVm.emitAction(
                            OrderActions.OnReservationRequest(
                                movie,
                                loginState.user
                            )
                        )
                    },
                    onDelete = { movie ->
                        navController.popBackStack()
                        movieVm.emitAction(MovieActions.DeleteMovie(movie))
                    },
                    asEmployee = loginState.user.role == Role.Employee
                )
            }
        }
        composable(Screens.EditMovie.name) {
            MovieEditScreen(
                state = editMovieVm.uiStateFlow.collectAsState().value,
                onConfirm = {
                    editMovieVm.emitEditAction(MovieEditActions.OnSaveChanges)
                    if (editMovieVm.uiStateFlow.value.errorMessage.isEmpty()) {
                        movieVm.emitAction(MovieActions.LoadCatalogue)
                        navController.popBackStack()
                    }
                },
                onValueChange = { action -> editMovieVm.emitEditAction(action) }
            )
        }

        // MY ORDERS NAVIGATION
        composable(Tab.MyOrders.route) {
            val loginState = loginVm.uiStateFlow.collectAsState().value
            if (loginState is LoginUiState.LoggedIn){
                orderVm.emitAction(OrderActions.OnLoadMyList(loginState.user.id))
                MyOrdersScreen(
                    myOrders = movieVm.assignMoviesToOrders(orderVm.getUsersOrders(loginState.user.id)),
                    toOrderMyOrderDetail = { orderAndMovie ->
                        orderVm.emitAction(
                            OrderActions.OnMyOrderDetailRequest(
                                order = orderAndMovie.first,
                                movie = orderAndMovie.second
                            )
                        )
                        navController.navigate(Screens.MyOrderDetail.name)
                    }
                )
            }
        }
        composable(Screens.MyOrderDetail.name) {
            val state = orderVm.uiStateFlow.collectAsState().value

            MyOrderDetailScreen(
                movie = state.movie,
                order = state.order,
                onMyOrderAction = { action -> orderVm.emitAction(action) }
            )
        }

        // ORDERS NAVIGATION
        composable(Tab.Orders.route) {
            orderVm.emitAction(OrderActions.OnLoadList)
            OrderScreen(
                orderData = orderVm.getOrderData(usersVm.getAllUsers(), movieVm.getAllMovies()),
                onOrderSelection = { triple ->
                    orderVm.emitAction(OrderActions.OnOrderDetailRequest(
                        order = triple.first, movie = triple.second, user = triple.third
                    ))
                    navController.navigate(Screens.OrderDetail.name)
                },
                onQueryRequest = {}
            )
        }
        composable(Screens.OrderDetail.name) {
            OrderDetailScreen(
                state = orderVm.uiStateFlow.collectAsState().value,
                onCloseOrder = { orderVm.emitAction(OrderActions.OnOrderFinish) }
            )
        }
        composable(Screens.CreateOrder.name) {
            CreateOrderScreen(
                state = orderCreatingVm.uiStateFlow.collectAsState().value,
                onMovieSelection = { navController.navigate(Screens.ChooseMovie.name) },
                onMovieClear = { orderCreatingVm.emitAction(CreateOrderActions.OnClearMovie) },
                onUserSelection = { navController.navigate(Screens.CreateOrder.name) },
                onUserClear = { orderCreatingVm.emitAction(CreateOrderActions.OnClearUser) },
                onConfirm = {
                    orderCreatingVm.emitAction(CreateOrderActions.OnCreateOrder)
                    navController.popBackStack()
                }
            )
        }
        composable(Screens.ChooseMovie.name) {
            movieVm.emitAction(MovieActions.LoadCatalogue)
            CatalogueScreen(
                state = movieVm.uiStateFlow.collectAsState().value,
                toMovieDetail = {
                    orderCreatingVm.emitAction(CreateOrderActions.OnMovieUpdate(it))
                    navController.popBackStack()
                },
                onQuerryRequest = { /*TODO*/ }
            )
        }
        composable(Screens.ChooseUser.name) {
            val loginState = loginVm.uiStateFlow.collectAsState().value
            if (loginState is LoginUiState.LoggedIn) {
                usersVm.emitAction(UserActions.OnUserList(loginState.user))
                UsersScreen(
                    users = usersVm.uiStateFlow.collectAsState().value.users,
                    toUserDetail = {
                        orderCreatingVm.emitAction(CreateOrderActions.OnUserUpdate(it))
                        navController.popBackStack()
                    }
                )
            }
        }

        // USERS NAVIGATION
        composable(Tab.Users.route) {
            val loginState = loginVm.uiStateFlow.collectAsState().value

            if (loginState is LoginUiState.LoggedIn) {
                usersVm.emitAction(UserActions.OnUserList(loginState.user))

                UsersScreen(
                    users = usersVm.getAllUsers(),
                    toUserDetail = { user ->
                        usersVm.emitAction(UserActions.OnUserDetail(user))
                        navController.navigate(Screens.UserDetail.name)
                    }
                )
            }
        }
        composable(Screens.UserDetail.name) {
            val displayedUser = usersVm.uiStateFlow.value.displayedUser ?: DEFAULT_USER
            val usersOrders = if (displayedUser != DEFAULT_USER) orderVm.getUsersOrders(displayedUser.id)
                else emptyList()

            UserDetailScreen(
                user = displayedUser,
                usersOrders = usersOrders,
                onPromoteClick = { usersVm.emitAction(UserActions.OnPromoteUser) },
                onDeleteClick = { user ->
                    navController.popBackStack()
                    orderVm.emitAction(OrderActions.OnDeleteUser(user.id))
                    usersVm.emitAction(UserActions.OnDeleteUser)
                }
            )
        }

        // ACCOUNT NAVIGATION
        composable(Screens.AccountDetail.name) {
            val loginState = loginVm.uiStateFlow.collectAsState().value
            if (loginState is LoginUiState.LoggedIn) {
                LoggedUserScreen(
                    user = loginState.user,
                    onUpdateAccountAction = { navController.navigate(Screens.EditAccount.name) },
                    onUpdatePasswordAction = { navController.navigate(Screens.EditPassword.name) }
                )
            }
        }
        composable(Screens.EditAccount.name) {
            val loginState = loginVm.uiStateFlow.collectAsState().value

            if (loginState is LoginUiState.LoggedIn) {
                AccountUpdateScreen(
                    loggedIn = loginState,
                    onValueChange = { action -> loginVm.emitActionLoggedIn(action) },
                    onConfirm = { loginVm.emitActionLoggedIn(UpdateAccountActions.OnConfirmUpdateData) },
                    onSuccess = {
                        loginVm.emitActionLoggedIn(UpdateAccountActions.DismissAlert)
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(Screens.EditPassword.name) {
            val loginState = loginVm.uiStateFlow.collectAsState().value

            if (loginState is LoginUiState.LoggedIn) {
                PasswordUpdateScreen(
                    loggedIn = loginState,
                    onValueChange = { action -> loginVm.emitActionLoggedIn(action) },
                    onConfirm = { loginVm.emitActionLoggedIn(UpdateAccountActions.OnConfirmUpdatePassword) },
                    onSuccess = {
                        loginVm.emitActionLoggedIn(UpdateAccountActions.DismissAlert)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

