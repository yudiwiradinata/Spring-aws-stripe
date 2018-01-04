$(document).ready(main);

function main() {

    $('.btn-collapse').click(function(e) {
        e.preventDefault();
        var $this = $(this);
        var $collapse = $this.closest('.collapse-group').find('.collapse');
        $collapse.collapse('toggle');
    })

    $('#contactForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        fields:{
            email:{
                validators:{
                    notEmpty:{
                        message:'The email is required'
                    },
                    emailAddress:{
                        message:'The input is not valid email address'
                    }
                }
            },
            firstName:{
                validators:{
                    notEmpty:{
                        message:'The first name is required'
                    }
                }
            },
            lastName:{
                validators:{
                    notEmpty:{
                        message:'The last name is required'
                    }
                }
            },
            feedback:{
                validators:{
                    notEmpty:{
                        message:'Your feedback is required'
                    }
                }
            }
        }
    });

    $('#savePasswordForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        fields:{
            password:{
                validators:{
                    notEmpty:{
                        message:'The password is required'
                    },
                    identical:{
                        field:'confirmPassword',
                        message:'The password and its confirmation are not same'
                    }
                }
            },
            confirmPassword:{
                validators:{
                    notEmpty:{
                        message:'The confirmation password is required'
                    },
                    identical:{
                        field:'password',
                        message:'The password and its confirmation are not same'
                    }
                }
            }
        }
    });

    $('#forgotPasswordForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        fields:{
            email:{
                validators:{
                    notEmpty:{
                        message:'The email is required'
                    },
                    emailAddress:{
                        message:'The input is not valid email address'
                    }
                }
            }
        }
    });

    $('#formLogin').formValidation({
        framework: 'bootstrap',
        icon: {
            valid : 'glyphicon glyphicon-ok',
            invalid : 'glyphicon glyphicon-remove',
            validating : 'glyphicon glyphicon-refresh'
        },
        fields:{
            username:{
                validators:{
                    notEmpty:{
                        message:'The username is required'
                    }
                }
            },
            password:{
                validators:{
                    notEmpty:{
                        message:'The password is required'
                    }
                }
            }
        }
    });
}