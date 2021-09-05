package com.alex.cryptoBackend.mapper;

import com.alex.cryptoBackend.dto.*;
import com.alex.cryptoBackend.model.*;
import com.alex.cryptoBackend.repository.TransactionRepository;
import com.alex.cryptoBackend.repository.UserRepository;
import com.alex.cryptoBackend.repository.WalletRepository;
import com.alex.cryptoBackend.security.service.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class MapMapper {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected TransactionRepository transactionRepository;
    @Autowired
    protected WalletRepository walletRepository;

    public abstract UserDetailsImpl toUserDetails(User user);
    public abstract RoleDto toDto(Role role);
    public abstract Role toRole(RoleDto roleDto);
    public abstract User toUser(UserDto userDto);
    public abstract User toUser(NewUserDto newUser);
    public abstract Currency toCurrency(CurrencyDto currencyDto);
    public abstract CurrencyDto toDto(Currency currency);
    public abstract List<CurrencyDto> toCurrencyListDto(List<Currency> currencies);
    @Mapping(target = "user", expression = "java(userRepository.findById(walletDto.getUserId()).orElseThrow(() -> new IllegalArgumentException(\"User doesn't exists\")))")
    public abstract Wallet toWallet(WalletDto walletDto);
    public abstract List<WalletDto> toWalletListDto(List<Wallet> wallets);
    @Mapping(target = "userId", expression = "java(wallet.getUser().getId())")
    public abstract WalletDto toDto(Wallet wallet);
    public abstract Set<WalletDto> walletDtoSet(Set<Wallet> wallets);
    public abstract UserDto toDto(User user);
    public abstract List<UserDto> toDto(List<User> users);

    @Mapping(target = "userSenderId", expression = "java(transaction.getSender().getUser().getId())")
    @Mapping(target = "userReceiverId", expression = "java(transaction.getReceiver().getUser().getId())")
    @Mapping(target = "walletSenderId", expression = "java(transaction.getSender().getId())")
    @Mapping(target = "walletReceiverId", expression = "java(transaction.getReceiver().getId())")
    @Mapping(target = "currency", expression = "java(transaction.getSender().getCurrency().getName())")
    public abstract TransactionDto toDto(Transaction transaction);

    @Mapping(target = "sender", expression = "java(walletRepository.findById(transactionDto.getWalletSenderId()).orElseThrow())")
    @Mapping(target = "receiver", expression = "java(walletRepository.findById(transactionDto.getWalletReceiverId()).orElseThrow())")
    public abstract Transaction toTransaction(TransactionDto transactionDto);
    public abstract List<TransactionDto> toTransactionDtoList(List<Transaction> transactions);
}
