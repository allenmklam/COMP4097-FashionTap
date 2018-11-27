package hk.edu.hkbu.comp.groupd.fashiontap

import android.databinding.ObservableField

class RegisterViewModel(
        val email:ObservableField<String> = ObservableField(""),
        val displayName: ObservableField<String> = ObservableField(""),
        val phoneNumber: ObservableField<String> = ObservableField(""),
        val password:ObservableField<String> = ObservableField(""),
        val passwordConfirm:ObservableField<String> = ObservableField("")
)